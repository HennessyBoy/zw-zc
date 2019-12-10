package com.hp.property.service.impl;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxRepairsMapper;
import com.hp.property.service.IZxRepairsService;
import com.hp.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产报修Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxRepairsServiceImpl implements IZxRepairsService
{

    @Autowired
    private ZxRepairsMapper zxRepairsMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;


    @Override
    public List<ZxChange> selectZxChangeByAssetId(Long id) {
        return zxRepairsMapper.selectZxChangeByAssetId(id);
    }

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        ZxAssetManagement management = zxRepairsMapper.selectZxAssetManagementById(id);
        Integer dept= management.getSubmittedDepartment();
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
    public int updateExtend3(ZxAssetManagement zxAssetManagement) {
        return zxRepairsMapper.updateExtend3(zxAssetManagement);
    }

    @Override
    public List<ZxChange> selectRepairsList(ZxChange zxChange) {

        String shareTime = zxChange.getShareTime();  //获取范围时间
        Map<String,Object> param = new HashMap<>();
        if(shareTime != null && !shareTime.equals("") && shareTime.length() != 0){
            String[] shareTimeArray = shareTime.split(" ~ ");
            for(int i=0;i<shareTimeArray.length;i++){
                System.out.println(shareTimeArray[i]);
            }
            param.put("beginTime",shareTimeArray[0]);
            param.put("endTime",shareTimeArray[1]);
        }
        zxChange.setParams(param);
        return zxRepairsMapper.selectRepairsList(zxChange);
    }

    @Override
    public List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement) {
        return zxRepairsMapper.selectAssetManagementList(zxAssetManagement);
    }
}
