package com.hp.property.mapper;

import com.hp.common.base.BaseMapper;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 资产变更Mapper接口
 * 
 * @author hp
 * @date 2019-09-02
 */
@MapperScan
public interface ZxChangeMapper extends BaseMapper<ZxChange>
{
    /**
     * 查询资产变更
     * 
     * @param id 资产变更ID
     * @return 资产变更
     */
    public ZxChange selectZxChangeById(Long id);

    /**
     * 查询资产变更列表
     * 
     * @param zxChange 资产变更
     * @return 资产变更集合
     */
    public List<ZxChange> selectZxChangeList(ZxChange zxChange);

    /**
     * 新增资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    public int insertZxChange(ZxChange zxChange);

    /**
     * 修改资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    public int updateZxChange(ZxChange zxChange);

    /**
     * 删除资产变更
     * 
     * @param id 资产变更ID
     * @return 结果
     */
    public int deleteZxChangeById(Long id);

    /**
     * 批量删除资产变更
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteZxChangeByIds(String[] ids);
    /**
     * 时间搜索记录
     */
    public List<ZxChange> getTimeChange(ZxChange zxChange);

    public List<ZxChange> selectZxChangeTransferList(ZxChange zxChange);

    Integer insertZxChangeByAssert(ZxAssetManagement zxAssetManagement);

    /**
     * 查询变更表中所有变动类型为1即领用的所有记录
     * @param zxChange
     * @return
     */
    List<ZxChange> selectAllChangeTypeOne(ZxChange zxChange);

    /**
     * 查询部门领用列表
     * @param zxChange
     * @return
     */
    List<ZxChange> selectDeptReceiveList(ZxChange zxChange);
}
