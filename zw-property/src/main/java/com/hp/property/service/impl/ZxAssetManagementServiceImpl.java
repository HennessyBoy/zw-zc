package com.hp.property.service.impl;

import com.hp.common.core.text.Convert;
import com.hp.common.exception.BusinessException;
import com.hp.common.utils.SnowFlake;
import com.hp.common.utils.StringUtils;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.mapper.ZxAssetManagementMapper;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.system.domain.SysUser;
import com.hp.system.mapper.SysDeptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 资产信息Service业务层处理
 *
 * @author hp
 * @date 2019-09-02
 */
@Service
public class ZxAssetManagementServiceImpl implements IZxAssetManagementService {

    private static final Logger log = LoggerFactory.getLogger(ZxAssetManagementServiceImpl.class);

    @Resource
    private ZxAssetManagementMapper zxAssetManagementMapper;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private SysDeptMapper deptMapper;

    /**
     * 查询资产信息
     *
     * @param id 资产信息ID
     * @return 资产信息
     */
    @Override
    public ZxAssetManagement selectZxAssetManagementById(Long id) {
        return zxAssetManagementMapper.selectZxAssetManagementById(id);
    }

    /**
     * 弹框列表
     * @param zxAssetManagement
     * @return
     */
    @Override
    public List<ZxAssetManagement> selectAssetManagementLists(ZxAssetManagement zxAssetManagement){
        return zxAssetManagementMapper.selectAssetManagementLists(zxAssetManagement);
    }

    /**
     * 弹框选择后显示在添加页面的列表
     * @param zxAssetManagement
     * @return
     */
    @Override
    public ZxAssetManagement selectAssetManagementListById(ZxAssetManagement zxAssetManagement){
        return zxAssetManagementMapper.selectAssetManagementListById(zxAssetManagement);
    }

    /**
     * 查询资产信息列表
     *
     * @param zxAssetManagement 资产信息
     * @return 资产信息
     */
    @Override
    public List<ZxAssetManagement> selectZxAssetManagementList(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectZxAssetManagementList(zxAssetManagement);
    }

    /**
     * 新增资产信息
     *
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    @Override
    public int insertZxAssetManagement(ZxAssetManagement zxAssetManagement) {
        int number = zxAssetManagement.getNumber();
        for (int i = 0; i < number; i++){
            //添加雪花算法 表id
            zxAssetManagement.setId(SnowFlake.nextId());
            int aNum = 0;
            int count = zxAssetManagementService.getCountByType(zxAssetManagement.getType());
            if (count != 0){
                //添加资产编号
                String maxNum = zxAssetManagementService.getMaxNum(zxAssetManagement);
                String substring = maxNum.substring(6);
                aNum = Integer.parseInt(substring);
            }
            zxAssetManagement.setAssetNum("WHHP-"+zxAssetManagement.getType()+ String.format("%05d",(aNum+1)));
            //添加入库时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());
            zxAssetManagement.setStorageTime(format);
            zxAssetManagement.setNumber(1);
            int a = zxAssetManagementMapper.insertZxAssetManagement(zxAssetManagement);
            if (a<1){
                return 0;
            }
        }
        return 1;
    }



    /**
     * 修改资产信息
     *
     * @param zxAssetManagement 资产信息
     * @return 结果
     */
    @Override
    public int updateZxAssetManagement(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.updateZxAssetManagement(zxAssetManagement);
    }

    /**
     * 删除资产信息对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteZxAssetManagementByIds(String ids) {
        return zxAssetManagementMapper.deleteZxAssetManagementByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除资产信息信息
     *
     * @param id 资产信息ID
     * @return 结果
     */
    public int deleteZxAssetManagementById(Long id) {
        return zxAssetManagementMapper.deleteZxAssetManagementById(id);
    }



    @Override
    public List<ZxAssetManagement> findAllStateOne(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectAllStateOne(zxAssetManagement);
    }

    @Override
    public List<ZxAssetManagement> seleAll(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectAAAStateTwo(zxAssetManagement);
    }

    @Override
    public Integer modifyZxAssertManagement(String ids) {
        return zxAssetManagementMapper.updateZxAssertManagement(Convert.toStrArray(ids));
    }

    // 查询变更表对应资产信息列表中领用的记录
    @Override
    public List<ZxAssetManagement> selectZxAssetManagementListById(ZxAssetManagement zxAssetManagement) {
        return zxAssetManagementMapper.selectZxAssetManagementListById(zxAssetManagement);
    }

    @Override
    public String getMaxNum(ZxAssetManagement zxAssetManagement) {

        return zxAssetManagementMapper.getMaxNum(zxAssetManagement);
    }

    @Override
    public int getCountByType(String type) {

        return zxAssetManagementMapper.getCountByType(type);
    }

    // 校区领用详情页
    @Override
    public ZxAssetManagement selectZxAssetManagementById2(Long id) {
        return zxAssetManagementMapper.selectZxAssetManagementById2(id);
    }

    /**
     * 导入
     * @param managementList
     * @param isUpdateSupport
     * @param operName
     * @return
     */
    @Override
    public String importZxAssetManagement(List<ZxAssetManagement> managementList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(managementList) || managementList.size() == 0)
        {
            throw new BusinessException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (ZxAssetManagement management : managementList)
        {
            try
            {
                String type = management.getType();  //资产类型
                if(type == null || type == "" || type.length() == 0){
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、资产 " + management.getName() + " 导入失败，请检查必填项");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、资产 " + management.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new BusinessException(failureMsg.toString());
        }
        else
        {
            for (ZxAssetManagement management1 : managementList)
            {
                try
                {
                    SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
                    Long schoolId = sysUser.getDeptId();  //获取部门编号（校区）
                    String place = management1.getPlace();  //存放地点名称
                    String type = management1.getType();  //资产类型
                    if(!isUpdateSupport){
                        int location = 0;
                        if(place != null && place != ""){
                            String jieguo = deptMapper.selectIdByName(place);
                            if(jieguo != null){
                                location = Integer.parseInt(jieguo);   //货物存放地点编号
                            }
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                        //购置时间
                        int legalLen = 10;
                        String purchasingTime = management1.getPurchasingTime();
                        if(purchasingTime != null){
                            if ((purchasingTime.length() != legalLen) && purchasingTime.length() != 0) {
                                Date date = new Date(purchasingTime);
                                management1.setPurchasingTime(dateFormat.format(date));
                            }
                        }
                        management1.setWarehousingCampus(new Long(schoolId).intValue());  //入库校区
                        management1.setCampus(new Long(schoolId).intValue());   //使用校区
                        management1.setLocation(location);  //存放地点
                        management1.setState(2);   //状态，2:在用
                        management1.setNumber(1);    //数量：默认1
                        management1.setExtend3("0");   //报修状态：0：正常
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
                        management1.setStorageTime(format.format(new Date()));  //入库时间
                        String loginName = ShiroUtils.getSysUser().getLoginName();  //获取登录人信息
                        management1.setOperator(loginName);  //操作人
                        this.insertZxAssetManagement(management1);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、资产 " + management1.getName() + " 导入成功");
                    }
                    else if (isUpdateSupport)
                    {
                        int location = 0;
                        if(place != null && place != ""){
                            String jieguo = deptMapper.selectIdByName(place);
                            if(jieguo != null){
                                location = Integer.parseInt(jieguo);   //货物存放地点编号
                            }
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                        //购置时间
                        int legalLen = 10;
                        if ((management1.getStorageTime().length() != legalLen) && management1.getStorageTime().length() != 0) {
                            Date date = new Date(management1.getStorageTime());
                            management1.setStorageTime(dateFormat.format(date));
                        }
                        management1.setWarehousingCampus(new Long(schoolId).intValue());  //入库校区
                        management1.setCampus(new Long(schoolId).intValue());   //使用校区
                        management1.setLocation(location);  //存放地点
                        management1.setState(2);   //状态，2:在用
                        management1.setNumber(1);    //数量：默认1
                        management1.setExtend3("0");   //报修状态：0：正常
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
                        management1.setStorageTime(format.format(new Date()));  //入库时间
                        String loginName = ShiroUtils.getSysUser().getLoginName();  //获取登录人信息
                        management1.setOperator(loginName);  //操作人
                        this.updateZxAssetManagement(management1);
                        successNum++;
                        successMsg.append("<br/>" + successNum + "、资产 " + management1.getName() + " 更新成功");
                    }
                    else
                    {
                        failureNum++;
                        failureMsg.append("<br/>" + failureNum + "、资产 " + management1.getName() + " 已存在");
                    }
                }
                catch (Exception e)
                {
                    failureNum++;
                    String msg = "<br/>" + failureNum + "、资产 " + management1.getName() + " 导入失败：";
                    failureMsg.append(msg + e.getMessage());
                    log.error(msg, e);
                }
            }
            if (failureNum > 0)
            {
                failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                throw new BusinessException(failureMsg.toString());
            }
            else
            {
                successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
            }
        }
        return successMsg.toString();
    }

}
