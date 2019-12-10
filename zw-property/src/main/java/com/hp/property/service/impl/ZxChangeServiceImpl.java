package com.hp.property.service.impl;

import com.hp.common.core.text.Convert;
import com.hp.common.utils.SnowFlake;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.mapper.ZxChangeMapper;
import com.hp.property.service.IZxChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产变更Service业务层处理
 * 
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxChangeServiceImpl implements IZxChangeService
{
    @Autowired(required = false)
    private ZxChangeMapper zxChangeMapper;
    @Autowired(required = false)
    private ZxAssetManagementMapper zxAssetManagementMapper;

    /**
     * 查询资产变更
     * 
     * @param id 资产变更ID
     * @return 资产变更
     */
    @Override
    public ZxChange selectZxChangeById(Long id)
    {
        return zxChangeMapper.selectZxChangeById(id);
    }

    /**
     * 查询资产变更列表
     * 
     * @param zxChange 资产变更
     * @return 资产变更
     */
    @Override
    public List<ZxChange> selectZxChangeList(ZxChange zxChange)
    {
        return zxChangeMapper.selectZxChangeList(zxChange);
    }

    /**
     * 新增资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    @Override
    public int insertZxChange(ZxChange zxChange) {
        return zxChangeMapper.insertZxChange(zxChange);
    }
    @Override
    public int insertZxChange2(ZxChange zxChange)
    {
        //雪花算法获取id
        Long id = SnowFlake.nextId();
        zxChange.setId(id);
        zxChange.setChangeType(2);   //2表示转移
        //在主表修改存放地点
        ZxAssetManagement z=new ZxAssetManagement();
        z.setLocation(Integer.parseInt(zxChange.getExtend3()));
        z.setAssetNum(zxChange.getAssetNum());
        zxAssetManagementMapper.updateZxAssetManagementByAssetNum(z);
        return zxChangeMapper.insertZxChange(zxChange);
    }

    /**
     * 修改资产变更
     * 
     * @param zxChange 资产变更
     * @return 结果
     */
    @Override
    public int updateZxChange(ZxChange zxChange)
    {
        return zxChangeMapper.updateZxChange(zxChange);
    }

    /**
     * 删除资产变更对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteZxChangeByIds(String ids)
    {
        return zxChangeMapper.deleteZxChangeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资产变更信息
     * 
     * @param id 资产变更ID
     * @return 结果
     */
    public int deleteZxChangeById(Long id)
    {
        return zxChangeMapper.deleteZxChangeById(id);
    }

    @Override
    public List<ZxChange> getTimeChange(ZxChange zxChange) {
        return zxChangeMapper.getTimeChange(zxChange);
    }


    public List<ZxChange> selectZxChangeTransferList(ZxChange zxChange){
        return zxChangeMapper.selectZxChangeTransferList(zxChange);
    }

    /**
     *  查询变动类型为1,即领用的所有信息
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> findAllChangeTypeOne(ZxChange zxChange) {
        return zxChangeMapper.selectAllChangeTypeOne(zxChange);
    }

    /**
     * 查询部门领用列表
     * @param zxChange
     * @return
     */
    @Override
    public List<ZxChange> selectDeptReceiveList(ZxChange zxChange){
        String shareTime = zxChange.getShareTime();  //获取范围时间
        Map<String,Object> param = new HashMap<>();
        if(shareTime != null && !shareTime.equals("") && shareTime.length() != 0){
            String[] shareTimeArray = shareTime.split(" ~ ");
            param.put("beginTime",shareTimeArray[0]);
            param.put("endTime",shareTimeArray[1]);
        }
        zxChange.setParams(param);
        return zxChangeMapper.selectDeptReceiveList(zxChange);
    }
}
