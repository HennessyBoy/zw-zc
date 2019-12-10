package com.hp.system.service;

import java.util.List;
import com.hp.common.core.domain.Ztree;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysRole;

/**
 * 部门管理 服务层
 * 
 * @author hp
 */
public interface ISysDeptService
{
    /**
     * 查询部门管理数据
     * 
     * @param dept 部门信息
     * @return 部门信息集合
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * 查询部门管理树
     * 
     * @param dept 部门信息
     * @return 所有部门信息
     */
    public List<Ztree> selectDeptTree(SysDept dept);

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    public List<Ztree> roleDeptTreeData(SysRole role);

    /**
     * 查询部门人数
     * 
     * @param parentId 父部门ID
     * @return 结果
     */
    public int selectDeptCount(Long parentId);

    /**
     * 查询部门是否存在用户
     * 
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * 删除部门管理信息
     * 
     * @param deptId 部门ID
     * @return 结果
     */
    public int deleteDeptById(Long deptId);

    /**
     * 新增保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int insertDept(SysDept dept);

    /**
     * 修改保存部门信息
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public int updateDept(SysDept dept);

    /**
     * 根据部门ID查询信息
     * 
     * @param deptId 部门ID
     * @return 部门信息
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param dept 部门信息
     * @return 结果
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * 查询部门通过父级id
     * @return
     */
    public List<SysDept> selectDeptByParentId();

    /**
     * 查询存放地点
     * @return
     */
    public List<SysDept> selectDeptByNotInParentId();

    /**
     * 校区领用使用（下拉框）
     * @return
     */
    public List<SysDept> selectSchoolByParentId();

    /**
     * 根据父级id查询子级信息（用于级联）
     * @param parentId
     * @return
     */
    public List<SysDept> selectPlaceByParentId(Long parentId);
}
