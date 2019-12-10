package com.hp.property.mapper;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * 资产报修Mapper接口
 * 
 * @author hp
 * @date 2019-09-02
 */
@MapperScan
public interface ZxRepairsMapper
{



    List<ZxChange> selectZxChangeByAssetId(Long id);

    ZxAssetManagement selectZxAssetManagementById(Long id);

    int updateExtend3(ZxAssetManagement zxAssetManagement);

    List<ZxChange> selectRepairsList(ZxChange zxChange);

    List<ZxAssetManagement> selectAssetManagementList(ZxAssetManagement zxAssetManagement);

}
