package com.hp.property.service.impl;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.mapper.ZxDiscardMapper;
import com.hp.property.service.IZxDiscardService;
import com.hp.system.domain.SysDept;
import com.hp.system.mapper.SysDeptMapper;
import com.hp.system.mapper.SysDictDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报废Service的实现类  IZxDiscardService
 */
@Service
public class ZxDiscardServiceImpl implements IZxDiscardService {
    @Autowired
    private ZxDiscardMapper zxDiscardMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;
    /**
     * 查询报废的变更记录信息列表
     *
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectDiscardList(ZxChange zxChange) {
        String shareTime = zxChange.getShareTime();  //获取范围时间
        Map<String,Object> param = new HashMap<>();
        if(shareTime != null && !shareTime.equals("") && shareTime.length() != 0){
            String[] shareTimeArray = shareTime.split(" ~ ");
            param.put("beginTime",shareTimeArray[0]);
            param.put("endTime",shareTimeArray[1]);
        }
        zxChange.setParams(param);
        List<ZxChange> changes = zxDiscardMapper.selectDiscardList(zxChange);
        return changes;
    }

    /**
     * 查询未报废的资产
     *
     * @param zxAssetManagement
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectZxNoDiscardList(ZxAssetManagement zxAssetManagement) {
        return zxDiscardMapper.selectNoDiscardZxAssetList(zxAssetManagement);
    }

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        ZxAssetManagement management = zxDiscardMapper.selectZxAssetManagementById(id);
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
}
