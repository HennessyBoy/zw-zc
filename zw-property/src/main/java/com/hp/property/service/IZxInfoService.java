package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 资产变更Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxInfoService
{

    List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    ZxChange selectZxChangeById(Long id);

    ZxAssetManagement selectZxAssetManagementById(Long id);
}
