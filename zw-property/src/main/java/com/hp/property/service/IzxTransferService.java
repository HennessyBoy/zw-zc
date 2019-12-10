package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 转移业务层接口
 */
public interface IzxTransferService {

    /**
     * 转移信息列表
     * @param zxChange
     * @return
     */
    List<ZxChange> selectTransferList(ZxChange zxChange);

    /**
     * 根据变更记录id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectZxAssetManagementById(Long id);

    /**
     * 查询资产信息列表（查询未转移的资产）
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> selectNoTransferList(ZxAssetManagement zxAssetManagement);

}
