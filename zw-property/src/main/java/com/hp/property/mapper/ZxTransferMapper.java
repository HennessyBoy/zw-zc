package com.hp.property.mapper;

import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * create by dell on 2019/9/17
 */
@MapperScan
public interface ZxTransferMapper {

    /**
     * 转移信息列表
     * @param zxChange
     * @return
     */
    List<ZxChange> selectTransferList(ZxChange zxChange);

    ZxAssetManagement selectZxAssetManagementById(Long id);

    List<ZxAssetManagement> selectNoTransferList(ZxAssetManagement zxAssetManagement);
}
