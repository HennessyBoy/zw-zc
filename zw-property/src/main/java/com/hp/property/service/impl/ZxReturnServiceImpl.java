package com.hp.property.service.impl;

import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxReturnMapper;
import com.hp.property.service.IZxReturnService;
import com.hp.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产退还Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxReturnServiceImpl implements IZxReturnService
{

    @Autowired
    private ZxAssetManagementMapper zxAssetManagementMapper;

    @Autowired
    private ZxChangeMapper zxChangeMapper;

    @Autowired
    private ZxReturnMapper zxReturnMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public List<ZxChange> selectZxReturnList(ZxChange zxChange,String campus) {
        //zxChange.setExtend5(campus);
        List<ZxChange> zxChanges = zxReturnMapper.selectZxAssetManagementList(zxChange);
        return zxChanges;
    }

    /**
     * 通过id查询详情
     * @param id
     * @return
     */
    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        ZxAssetManagement management = zxReturnMapper.selectZxAssetManagementById(id);
        Integer dept = management.getSubmittedDepartment();
        String department = "";
        if(!"".equals(dept) && dept != null){
            department = sysDictDataMapper.selectDictLabel("zc_department",dept.toString());
        }
        management.setDepartmentName(department);
        Integer state = management.getState();
        String status = "";
        if(!"".equals(state) && state != null){
            status = sysDictDataMapper.selectDictLabel("zc_state",state.toString());
        }
        management.setStatus(status);
        return management;
    }

    @Override
    public int insertManagementAndChange(ZxChange zxChange,Long[] Ids) {
        for(int a=0;a<Ids.length;a++){
           Long id= Ids[a];
           zxChange.setAssetsId(id);
            Long cid = SnowFlake.nextId();
            zxChange.setId(cid);
            zxChange.setChangeType(5);
            int i= zxReturnMapper.updateManagementStateById(id);
            if(i>0){
                zxReturnMapper.insertChange(zxChange);

            }else{
                return 0;
            }
        }
        return 1;

    }

    @Override
    public List<ZxChange> selectZxChangeById(Long id) {
        return zxReturnMapper.selectZxChangeByAssetsId(id);
    }

    @Override
    public List<ZxAssetManagement> selectManagementList(ZxChange zxChange, ZxAssetManagement zxAssetManagement) {
        if(zxChange!=null){
            if(zxChange.getExtend1()!=null){ zxAssetManagement.setExtend1(zxChange.getExtend1());}
            if(zxChange.getUseDepartment()!=null){  zxAssetManagement.setExtend2(zxChange.getUseDepartment().toString());}
            if(zxChange.getUsers()!=null){  zxAssetManagement.setExtend3(zxChange.getUsers());}
        }
        return zxReturnMapper.selectManagementList(zxAssetManagement);
    }

    /**
     * 查询退还信息
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectReturnList(ZxChange zxChange){
        String shareTime = zxChange.getShareTime();  //获取范围时间
        Map<String,Object> param = new HashMap<>();
        if(shareTime != null && !shareTime.equals("") && shareTime.length() != 0){
            String[] shareTimeArray = shareTime.split(" ~ ");
            param.put("beginTime",shareTimeArray[0]);
            param.put("endTime",shareTimeArray[1]);
        }
        zxChange.setParams(param);
        return zxReturnMapper.selectReturnList(zxChange);
    }

    /**
     * 查询在用状态列表
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement){
        return zxReturnMapper.selectAssetManagementList(zxAssetManagement);
    }

}
