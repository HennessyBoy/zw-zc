package com.hp.property.service;

import com.hp.property.domain.ZxAssetManagement;
import java.util.List;

/**
 * 资产信息Service接口
 * 
 * @author hp
 * @date 2019-09-02
 */
public interface IZxAssetManagementService 
{
    /**
     * 查询资产信息
     * 
     * @param id 资产信息ID
     * @return 资产信息
     */
    public ZxAssetManagement selectZxAssetManagementById(Long id);

    /**
     * 弹框列表
     * @param zxAssetManagement
     * @return
     */
    public List<ZxAssetManagement> selectAssetManagementLists(ZxAssetManagement zxAssetManagement);

    /**
     * 弹框选择后显示在添加页面的列表
     * @param zxAssetManagement
     * @return
     */
    public ZxAssetManagement selectAssetManagementListById(ZxAssetManagement zxAssetManagement);

    /**
     * 查询资产信息列表
     * 
     * @param zxAssetManagement 资产信息
     * @return 资产信息集合
     */
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    /**
     * 新增资产信息
     * 
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    public int insertZxAssetManagement(ZxAssetManagement zxAssetManagement);

    /**
     * 修改资产信息
     * 
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    public int updateZxAssetManagement(ZxAssetManagement zxAssetManagement);

    /**
     * 批量删除资产信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteZxAssetManagementByIds(String ids);

    /**
     * 删除资产信息信息
     * 
     * @param id 资产信息ID
     * @return 结果
     */
    public int deleteZxAssetManagementById(Long id);



    /**
     * 查询所有资产为闲置状态的信息
     * @param zxAssetManagement
     * @return
     */
    List<ZxAssetManagement> findAllStateOne(ZxAssetManagement zxAssetManagement);

    List<ZxAssetManagement> seleAll(ZxAssetManagement zxAssetManagement);

    /**
     * 根据id修改资产状态为领用
     * @param ids
     * @return
     */
    Integer modifyZxAssertManagement(String ids);

    /**
     * 查询变更表对应资产信息列表中领用的记录
     * @param zxAssetManagement 资产信息
     * @return 资产信息集合
     */
    List<ZxAssetManagement> selectZxAssetManagementListById(ZxAssetManagement zxAssetManagement);

     String getMaxNum(ZxAssetManagement zxAssetManagement);

     int getCountByType(String type);

     // 校区领用详情页
    ZxAssetManagement selectZxAssetManagementById2(Long id);

    /**
     * 导入
     * @param managementList
     * @param isUpdateSupport
     * @param operName
     * @return
     */
    public String importZxAssetManagement(List<ZxAssetManagement> managementList, Boolean isUpdateSupport, String operName);
}
