package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 资产报修Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxRepairsService
{



    List<ZxChange> selectZxChangeByAssetId(Long id);

    ZxAssetManagement selectZxAssetManagementById(Long id);

    int updateExtend3(ZxAssetManagement assetManagement);

    /**
     * 查询所有报修数据
     * @param zxChange
     * @return
     */
    List<ZxChange> selectRepairsList(ZxChange zxChange);

    List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement);

}
