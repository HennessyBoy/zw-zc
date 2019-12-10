package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;

import java.util.List;

/**
 * 资产退还Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxReturnService
{

    List<ZxChange> selectZxReturnList(ZxChange zxChange,String campus);

    /**
     * 通过id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectZxAssetManagementById(Long id);

    int insertManagementAndChange(ZxChange zxChange, Long[] Ids);

    List<ZxChange> selectZxChangeById(Long id);

    List<ZxAssetManagement> selectManagementList(ZxChange zxChange2, ZxAssetManagement zxAssetManagement);

    /**
     * 查询退还信息
     * @param zxChange
     * @return
     */
    List<ZxChange> selectReturnList(ZxChange zxChange);

    /**
     * 查询在用状态列表
     * @return
     */
    public List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement);
}
