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
public interface ZxInfoMapper extends BaseMapper<ZxAssetManagement>
{

    List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement);

    ZxChange selectZxChangeById(Long id);

    ZxAssetManagement selectZxAssetManagementById(Long id);
}
