package com.hp.property.domain;

import com.hp.common.annotation.Excel;
import com.hp.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Id;
import java.util.Date;

/**
 * 资产变更对象 zx_change
 * 
 * @author hp
 * @date 2019-09-02
 */
public class ZxChange extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Id
    private Long id;

    /** 资产id */
    @Excel(name = "资产id")
    private Long assetsId;

    /** 变动类型 */
    @Excel(name = "变动类型")
    private Integer changeType;

    /** 提交部门 */
    @Excel(name = "提交部门")
    private Integer submittedDepartment;

    /** 提交人 */
    @Excel(name = "提交人")
    private String submitOne;

    /** 使用部门 */
    @Excel(name = "使用部门")
    private Integer useDepartment;

    /** 使用人 */
    @Excel(name = "使用人")
    private String users;

    /** 创建时间 */
    private String extend1;
    /** 接收人员 */
    private String extend2;
    /** 存放地点 */
    private String extend3;
    /** 使用校区 */
    private String extend4;
    /** 校区编号 */
    private Long extend5;
    /** 存储退还时间，报废时间等共用字段 */
    private String startTime;
    private String oneTime;
    private String twoTime;

    //资产编号
    private String assetNum;
    //资产名称
    private String name;
    //资产分类
    private String type;
    //品牌
    private String brand;
    //规格型号
    private String assetSpecification;
    // 数量
    private Integer number;
    //单位
    private String  units;
    //编号字符串
    private String ids;

    /** 时间 */
    private String shareTime;

    /** 校区 */
    private String deptName;
    /** 存放地点（显示用） */
    private String place;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getAssetNum() {
        return assetNum;
    }

    public void setAssetNum(String assetNum) {
        this.assetNum = assetNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAssetSpecification() {
        return assetSpecification;
    }

    public void setAssetSpecification(String assetSpecification) {
        this.assetSpecification = assetSpecification;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        try {
            if (startTime != null && startTime != ""){
                this.setOneTime(startTime.substring(0,10));
                this.setTwoTime(startTime.substring(13));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOneTime() {
        return oneTime;
    }

    public void setOneTime(String oneTime) {
        this.oneTime = oneTime;
    }

    public String getTwoTime() {
        return twoTime;
    }

    public void setTwoTime(String twoTime) {
        this.twoTime = twoTime;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setAssetsId(Long assetsId) 
    {
        this.assetsId = assetsId;
    }

    public Long getAssetsId() 
    {
        return assetsId;
    }
    public void setChangeType(Integer changeType) 
    {
        this.changeType = changeType;
    }

    public Integer getChangeType() 
    {
        return changeType;
    }
    public void setSubmittedDepartment(Integer submittedDepartment) 
    {
        this.submittedDepartment = submittedDepartment;
    }

    public Integer getSubmittedDepartment() 
    {
        return submittedDepartment;
    }
    public void setSubmitOne(String submitOne) 
    {
        this.submitOne = submitOne;
    }

    public String getSubmitOne() 
    {
        return submitOne;
    }
    public void setUseDepartment(Integer useDepartment) 
    {
        this.useDepartment = useDepartment;
    }

    public Integer getUseDepartment() 
    {
        return useDepartment;
    }
    public void setUsers(String users) 
    {
        this.users = users;
    }

    public String getUsers() 
    {
        return users;
    }
    public void setExtend1(String extend1) 
    {
        this.extend1 = extend1;
    }

    public String getExtend1() 
    {
        return extend1;
    }
    public void setExtend2(String extend2) 
    {
        this.extend2 = extend2;
    }

    public String getExtend2() 
    {
        return extend2;
    }
    public void setExtend3(String extend3) 
    {
        this.extend3 = extend3;
    }

    public String getExtend3() 
    {
        return extend3;
    }
    public void setExtend4(String extend4) 
    {
        this.extend4 = extend4;
    }

    public String getExtend4() 
    {
        return extend4;
    }
    public void setExtend5(Long extend5)
    {
        this.extend5 = extend5;
    }

    public Long getExtend5()
    {
        return extend5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("assetsId", getAssetsId())
            .append("changeType", getChangeType())
            .append("submittedDepartment", getSubmittedDepartment())
            .append("submitOne", getSubmitOne())
            .append("useDepartment", getUseDepartment())
            .append("users", getUsers())
            .append("extend1", getExtend1())
            .append("extend2", getExtend2())
            .append("extend3", getExtend3())
            .append("extend4", getExtend4())
            .append("extend5", getExtend5())
            .toString();
    }
}
