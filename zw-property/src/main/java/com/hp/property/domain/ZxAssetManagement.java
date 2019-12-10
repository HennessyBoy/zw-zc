package com.hp.property.domain;

import com.hp.common.annotation.Excel;
import com.hp.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Id;
import java.util.Date;

/**
 * 资产信息对象 zx_asset_management
 * 
 * @author hp
 * @date 2019-09-02
 */
public class ZxAssetManagement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资产ID */
    @Id
    private Long id;

    /** 资产编号 */
    //@Excel(name = "资产编号")
    private String assetNum;

    /** 资产名称 */
    @Excel(name = "资产名称")
    private String name;

    /** 品牌 */
    @Excel(name = "品牌")
    private String brand;

    /** 资产规格 */
    @Excel(name = "规格型号")
    private String assetSpecification;

    /** 数量 */
    //@Excel(name = "数量")
    private Integer number;

    /** 资产类型 */
    @Excel(name = "资产类型", readConverterExp = "A=办公设备类,B=办公家私类,C=运输设备类,D=电器设备类,E=机器设备类,F=其它设备类")
    private String  type;

    /** 单位 */
    @Excel(name = "单位", readConverterExp = "1=个,2=台,3=张,4=把,5=本,6=盒,7=罐,8=块,9=付,10=套,11=双,12=只,13=件,14=卷,15=辆,16=支")
    private Integer units;

    /** 供货商 */
    @Excel(name = "供货商")
    private String supplier;

    /** 图片 */
    //@Excel(name = "图片")
    private String picture;

    /** 资产原值 */
    @Excel(name = "资产原值")
    private Long price;

    /** 购置时间 */
    @Excel(name = "购置时间")
    private String purchasingTime;

    /** 入库时间 */
    //@Excel(name = "入库时间")
    private String storageTime;

    /** 入库校区 */
    //@Excel(name = "入库校区")
    private Integer warehousingCampus;

    /** 状态 */
    private Integer state;

    /** 操作人员 */
    private String operator;

    /** 存放地点 */
    private Integer location;

    /** 领用时间 */
    private Date recipientsTime;

    /** 报修时间 */
    private Date repairTime;

    /** 转移时间 */
    private Date transferTime;

    /** 报废时间 */
    private Date discardTime;

    /** 退还时间 */
    private Date returnTime;

    /** 校区 */
    //@Excel(name = "校区")
    private Integer campus;

    /** 入库校区名称 */
    private String branch;
    /** 部门 */
    private Integer department;

    /** 使用部门名称 */
    @Excel(name = "使用部门", readConverterExp = "1=董事长,2=行政部,3=财务部,4=市场管理部,5=人力资源部,6=河南院校,7=渠道部,8=网络推广部,9=咨询部,10=教学管理部,11=市场活动部,12=品质管理部,13=武汉青鸟之家,14=荆州青鸟之家,15=襄阳青鸟之家")
    private String extend1;

    /** 使用人 */
    @Excel(name = "使用人")
    private String extend2;

    /** $column.columnComment */
    private String extend3;

    /** $column.columnComment */
    private String extend4;

    private String extend5;

    /** 用于删除时做判断 */
    private Integer num;
    /** 接收人员 */
    private String reception;
    /** 存放地点名称 */
    @Excel(name = "存放地点")
    private String place;
    /** 领用部门 */
    private Integer submittedDepartment;
    /** 领用人员 */
    private String submitOne;

    public Integer getSubmittedDepartment() {
        return submittedDepartment;
    }

    public void setSubmittedDepartment(Integer submittedDepartment) {
        this.submittedDepartment = submittedDepartment;
    }

    public String getSubmitOne() {
        return submitOne;
    }

    public void setSubmitOne(String submitOne) {
        this.submitOne = submitOne;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getReception() {
        return reception;
    }

    public void setReception(String reception) {
        this.reception = reception;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    private ZxChange zxChange;

    private String ids;

    private String startTime;
    private String oneTime;
    private String twoTime;

    /** 共用时间 */
    private String shareTime;

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    /** 部门编号 */
    private Long useDepartment;
    //private Long submittedDepartment;

    /** 退还（使用）人 */
    private String users;
    /** 校区名称 */
    private String deptName;
    /** 部门名称 */
    private String departmentName;
    /** 状态名称 */
    private String status;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getUseDepartment() {
        return useDepartment;
    }

    public void setUseDepartment(Long useDepartment) {
        this.useDepartment = useDepartment;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        try {
            this.setOneTime(startTime.substring(0,10));
            this.setTwoTime(startTime.substring(13));
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public ZxChange getZxChange() {
        return zxChange;
    }

    public void setZxChange(ZxChange zxChange) {
        this.zxChange = zxChange;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setAssetNum(String assetNum) 
    {
        this.assetNum = assetNum;
    }

    public String getAssetNum() 
    {
        return assetNum;
    }
    public void setBrand(String brand) 
    {
        this.brand = brand;
    }

    public String getBrand() 
    {
        return brand;
    }
    public void setAssetSpecification(String assetSpecification) 
    {
        this.assetSpecification = assetSpecification;
    }

    public String getAssetSpecification() 
    {
        return assetSpecification;
    }
    public void setNumber(Integer number) 
    {
        this.number = number;
    }

    public Integer getNumber() 
    {
        return number;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setUnits(Integer units) 
    {
        this.units = units;
    }

    public Integer getUnits() 
    {
        return units;
    }
    public void setSupplier(String supplier) 
    {
        this.supplier = supplier;
    }

    public String getSupplier() 
    {
        return supplier;
    }
    public void setPicture(String picture) 
    {
        this.picture = picture;
    }

    public String getPicture() 
    {
        return picture;
    }
    public void setPrice(Long price) 
    {
        this.price = price;
    }

    public Long getPrice() 
    {
        return price;
    }

    public String getPurchasingTime() {
        return purchasingTime;
    }

    public void setPurchasingTime(String purchasingTime) {
        this.purchasingTime = purchasingTime;
    }

    public String getStorageTime() {
        return storageTime;
    }

    public void setStorageTime(String storageTime) {
        this.storageTime = storageTime;
    }

    public void setWarehousingCampus(Integer warehousingCampus)
    {
        this.warehousingCampus = warehousingCampus;
    }

    public Integer getWarehousingCampus() 
    {
        return warehousingCampus;
    }
    public void setState(Integer state) 
    {
        this.state = state;
    }

    public Integer getState() 
    {
        return state;
    }
    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }
    public void setLocation(Integer location) 
    {
        this.location = location;
    }

    public Integer getLocation() 
    {
        return location;
    }
    public void setRecipientsTime(Date recipientsTime) 
    {
        this.recipientsTime = recipientsTime;
    }

    public Date getRecipientsTime() 
    {
        return recipientsTime;
    }
    public void setRepairTime(Date repairTime) 
    {
        this.repairTime = repairTime;
    }

    public Date getRepairTime() 
    {
        return repairTime;
    }
    public void setTransferTime(Date transferTime) 
    {
        this.transferTime = transferTime;
    }

    public Date getTransferTime() 
    {
        return transferTime;
    }
    public void setDiscardTime(Date discardTime) 
    {
        this.discardTime = discardTime;
    }

    public Date getDiscardTime() 
    {
        return discardTime;
    }
    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }
    public void setCampus(Integer campus) 
    {
        this.campus = campus;
    }

    public Integer getCampus() 
    {
        return campus;
    }
    public void setDepartment(Integer department) 
    {
        this.department = department;
    }

    public Integer getDepartment() 
    {
        return department;
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
    public void setExtend5(String extend5) 
    {
        this.extend5 = extend5;
    }

    public String getExtend5() 
    {
        return extend5;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("assetNum", getAssetNum())
            .append("brand", getBrand())
            .append("assetSpecification", getAssetSpecification())
            .append("number", getNumber())
            .append("type", getType())
            .append("units", getUnits())
            .append("supplier", getSupplier())
            .append("picture", getPicture())
            .append("price", getPrice())
            .append("purchasingTime", getPurchasingTime())
            .append("storageTime", getStorageTime())
            .append("warehousingCampus", getWarehousingCampus())
            .append("state", getState())
            .append("operator", getOperator())
            .append("location", getLocation())
            .append("recipientsTime", getRecipientsTime())
            .append("repairTime", getRepairTime())
            .append("transferTime", getTransferTime())
            .append("discardTime", getDiscardTime())
            .append("returnTime", getReturnTime())
            .append("campus", getCampus())
            .append("department", getDepartment())
            .append("extend1", getExtend1())
            .append("extend2", getExtend2())
            .append("extend3", getExtend3())
            .append("extend4", getExtend4())
            .append("extend5", getExtend5())
            .toString();
    }

    /*public Long getSubmittedDepartment() {
        return submittedDepartment;
    }

    public void setSubmittedDepartment(Long submittedDepartment) {
        this.submittedDepartment = submittedDepartment;
    }*/
}
