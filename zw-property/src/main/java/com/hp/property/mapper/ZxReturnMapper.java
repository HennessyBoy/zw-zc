package com.hp.property.mapper;

import com.hp.common.base.BaseMapper;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 资产退还Mapper接口
 * 
 * @author hp
 * @date 2019-09-02
 */
@MapperScan
public interface ZxReturnMapper
{

    List<ZxChange> selectZxChangeByAssetsId(Long id);

    /**
     * 查询退还信息
     * @param zxChange
     * @return
     */
    List<ZxChange> selectReturnList(ZxChange zxChange);

    /**'
     * 通过id查询详情
     * @param id
     * @return
     */
    ZxChange selectDetailById(Long id);

    List<ZxChange> selectZxAssetManagementList(ZxChange zxChange);

    /**
     * 通过id查询详情
     * @param id
     * @return
     */
    ZxAssetManagement selectZxAssetManagementById(Long id);

    /**
     * 查询在用状态列表
     * @return
     */
    List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement);

    int updateManagementStateById(Long id);

    int insertChange(ZxChange zxChange);

    List<ZxAssetManagement> selectManagementList(ZxAssetManagement zxAssetManagement);

    /* List<ZxAssetManagement> selectZxAssetManagementsByIds(Long[] ids);*/
}
