package com.hp.property.service.impl;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxInfoMapper;
import com.hp.property.service.IZxInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产变更Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxInfoServiceImpl implements IZxInfoService
{
    @Autowired
    private ZxInfoMapper zxInfoMapper;

    @Override
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement) {
        return zxInfoMapper.selectZxAssetManagementList(zxAssetManagement);
    }

    @Override
    public ZxChange selectZxChangeById(Long id) {
        return zxInfoMapper.selectZxChangeById(id);
    }

    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        return zxInfoMapper.selectZxAssetManagementById(id);
    }
}
