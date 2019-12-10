package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 报废报废Service的接口  IZxDiscardService
 */
public interface IZxDiscardService {

    /**
     * 查询报废信息列表
     * @param zxChange
     * @return
     */
    List<ZxChange> selectDiscardList(ZxChange zxChange);


    /**
     * 查询未报废的资产
     *
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectZxNoDiscardList(ZxAssetManagement zxAssetManagement);


    /**
     * 通过id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectZxAssetManagementById(Long id);
}
