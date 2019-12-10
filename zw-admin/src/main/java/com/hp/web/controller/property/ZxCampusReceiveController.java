package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxReturnService;
import com.hp.property.service.impl.ZxChangeServiceImpl;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 部门领用
 * @author Liushun
 * @date Created in 2019/9/2 18:00
 * @description
 */

@Controller
@RequestMapping("/property/campusrecive")
public class ZxCampusReceiveController extends BaseController {

    private String prefix = "property/campusrecive";

    @Autowired
    private ZxChangeServiceImpl zxChangeService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysDeptService iSysDeptService;

    @Autowired
    private IZxReturnService zxReturnService;


    // 跳转到部门领用主页面
    @RequiresPermissions("property:campusrecive:view")
    @GetMapping()
    public String management(ModelMap mmap)
    {
        List<SysDept> sysDepts = iSysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        return prefix + "/campusrecive";
    }

    /**
     * 查询部门领用主页面即领用资产信息列表
     */
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange)
    {
        startPage();
        List<ZxChange> list = zxChangeService.findAllChangeTypeOne(zxChange);
        return getDataTable(list);
    }


    /**
     * 新增资产信息
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request,ModelMap mmap)
    {
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        List<SysDept> sysDepts = iSysDeptService.selectSchoolByParentId();
        mmap.put("school",sysDepts);

        SysDept dept = new SysDept();
        SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
        Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
        List<SysDept> deptList = null;
        if(schoolId == 100){
            deptList = iSysDeptService.selectDeptByNotInParentId();
        }else {
            dept.setParentId(schoolId);
            deptList = iSysDeptService.selectDeptList(dept);
        }
        mmap.put("deptList", deptList);
        return prefix + "/add";
    }


   // 新增保存资产信息
    @RequiresPermissions("property:campusrecive:add")
    @Log(title = "资产信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange,HttpSession session)
    {
        String ids = session.getAttribute("s").toString();
        int i1=0;
        if (ids != null && !ids.equals("")){
            ZxAssetManagement zxone = null;
            Set set = new HashSet();
            String[] split = ids.split(",");
            for (int i=0; i<split.length; i++){
                set.add(split[i]);
            }
            set.remove("0");
            set.remove("");
            for(Object id:set){
                String assetId = id.toString();  //获取单个id
                zxone = new ZxAssetManagement();  //创建ZxAssetManagement表对象（用于传参）
                zxone.setId(Long.parseLong(assetId));  //单个id
                if(zxChange.getExtend3() != null){
                    zxone.setLocation(Integer.parseInt(zxChange.getExtend3()));  //存放地点
                }
                if(zxChange.getExtend5() != null){
                    Long campus = zxChange.getExtend5();
                    zxone.setCampus(new Long(campus).intValue());   //所属部门
                }

                long l = SnowFlake.nextId();
                zxChange.setId(l);
                zxChange.setAssetsId(Long.parseLong(assetId));  //主表id（资产表）
                zxChange.setChangeType(1);   //1：部门领用
                zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));  //创建时间

                zxChangeService.insertZxChange(zxChange);
                i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
            }
            session.removeAttribute("s");//清空session信息
            return toAjax(i1);
        }
        session.removeAttribute("s");//清空session信息
        return toAjax(zxChangeService.insertZxChange(null));
    }

    // 查看资产信息详情
    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxReturnService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/detail";
    }

    // 跳转到闲置资产信息页面
    @GetMapping("/showIdle")
    public String showIdle()
    {
        return prefix + "/showIdle";
    }

    // 查询闲置资产信息列表
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list1")
    @ResponseBody
    public TableDataInfo list1(ZxAssetManagement zxAssetManagement)
    {
        // 资产状态1代表闲置
        zxAssetManagement.setState(1);
        // 未保修的
        zxAssetManagement.setExtend3("0");

        // 带条件查询所有
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectAssetManagementLists(zxAssetManagement);
        return getDataTable(list);
    }


    // 查询所有选中的闲置资产信息
    @RequiresPermissions("property:campusrecive:list")
    @PostMapping("/list3")
    @ResponseBody
    public TableDataInfo list3(ZxAssetManagement zxAssetManagement, HttpServletRequest request)
    {
        //获取num（用于删除做判断：num=0删除状态,num=-1正常添加状态）
        int num = zxAssetManagement.getNum();
        if(num == 0){
            //清空session信息
            request.getSession().removeAttribute("s");

            if (zxAssetManagement.getIds()!=null&&!zxAssetManagement.getIds().equals("")){
                List<ZxAssetManagement> list=new LinkedList<>();
                String s=zxAssetManagement.getIds();
                HttpSession session=request.getSession();
                if(session.getAttribute("s")==null){
                    session.setAttribute("s","0");
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }else{
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }
                String spl=session.getAttribute("s").toString();
                Set set = new HashSet();
                String[] split =spl.split(",");
                for (int i=0;i<split.length;i++){
                    set.add(split[i]);
                }
                set.remove("0");
                set.remove("");
                for(Object id:set){
                    String s1 = id.toString();
                    if(!s1.equals("")){
                        zxAssetManagement.setId(Long.parseLong(s1));
                        ZxAssetManagement ls = zxAssetManagementService.selectAssetManagementListById(zxAssetManagement);
                        list.add(ls);
                    }
                }
                return getDataTable(list);
            }else {
                List<ZxAssetManagement> list=new LinkedList<>();
                return getDataTable(list);
            }
        }else{
            if (zxAssetManagement.getIds()!=null&&!zxAssetManagement.getIds().equals("")){
                List<ZxAssetManagement> list=new LinkedList<>();
                String s=zxAssetManagement.getIds();
                HttpSession session=request.getSession();
                if(session.getAttribute("s")==null){
                    session.setAttribute("s","0");
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }else{
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }
                String spl=session.getAttribute("s").toString();
                Set set = new HashSet();
                String[] split =spl.split(",");
                for (int i=0;i<split.length;i++){
                    set.add(split[i]);
                }
                set.remove("0");
                set.remove("");
                for(Object id:set){
                    String s1 = id.toString();
                    if(!s1.equals("")){
                        zxAssetManagement.setId(Long.parseLong(s1));
                        ZxAssetManagement ls = zxAssetManagementService.selectAssetManagementListById(zxAssetManagement);
                        list.add(ls);
                    }
                }
                return getDataTable(list);
            }else {
                List<ZxAssetManagement> list=new LinkedList<>();
                return getDataTable(list);
            }
        }
    }
}
