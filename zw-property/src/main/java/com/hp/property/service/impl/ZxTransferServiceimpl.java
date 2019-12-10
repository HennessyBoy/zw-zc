package com.hp.property.service.impl;/**
 * create by dell on 2019/9/17
 */

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxTransferMapper;
import com.hp.property.service.IzxTransferService;
import com.hp.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转移实现类
 * @date : 2019-09-17 10:25
 **/
@Service
public class ZxTransferServiceimpl implements IzxTransferService{

    @Autowired
    private ZxTransferMapper zxTransferMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    /**
     * 转移信息列表
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectTransferList(ZxChange zxChange) {
        String shareTime = zxChange.getShareTime();  //获取范围时间
        Map<String,Object> param = new HashMap<>();
        if(shareTime != null && !shareTime.equals("") && shareTime.length() != 0){
            String[] shareTimeArray = shareTime.split(" ~ ");
            param.put("beginTime",shareTimeArray[0]);
            param.put("endTime",shareTimeArray[1]);
        }
        zxChange.setParams(param);
        return zxTransferMapper.selectTransferList(zxChange);
    }

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        ZxAssetManagement management = zxTransferMapper.selectZxAssetManagementById(id);
        System.out.println(management);
        Long dept= management.getUseDepartment();
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

    /**
     * 查询未报废的资产
     *
     * @param zxAssetManagement
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectNoTransferList(ZxAssetManagement zxAssetManagement) {
        return zxTransferMapper.selectNoTransferList(zxAssetManagement);
    }

}
