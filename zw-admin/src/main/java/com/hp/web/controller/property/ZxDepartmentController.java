package com.hp.web.controller.property;

/**
 * 部门领用控制层
 */
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
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxReturnService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/property/department")
public class ZxDepartmentController extends BaseController {

    private String prefix="property/department";

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private IZxReturnService zxReturnService;

    @Autowired
    private ISysDeptService sysDeptService;

    @RequiresPermissions("property:department:view")
    @GetMapping()
    public String department(ModelMap mmap){
        List<SysDept> sysDepts = sysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        return prefix+"/department";
    }

    /**
     * 查询部门领用列表
     */
    @RequiresPermissions("property:department:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange)
    {
        Long campus = ShiroUtils.getSysUser().getDeptId();
        if (campus != 100){
            zxChange.setExtend5(campus);
        }
        startPage();
        List<ZxChange> list = zxChangeService.selectDeptReceiveList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增部门领用
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request,ModelMap mmap)
    {
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        SysDept dept = new SysDept();
        SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
        Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
        List<SysDept> deptList = null;
        if(schoolId == 100){
            deptList = sysDeptService.selectDeptByNotInParentId();
        }else {
            dept.setParentId(schoolId);
            deptList = sysDeptService.selectDeptList(dept);
        }
        mmap.put("deptList", deptList);
        return prefix + "/add";
    }

    /**
     * 新增保存部门领用
     */
    @RequiresPermissions("property:department:add")
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
                zxone.setState(2);   //状态（1：闲置，2：在用，3：报废）
                if(zxChange.getSubmittedDepartment() != null){
                    zxone.setExtend1(zxChange.getSubmittedDepartment().toString());  //使用部门
                    zxChange.setUseDepartment(zxChange.getSubmittedDepartment());
                }
                zxone.setExtend2(zxChange.getUsers());  //使用人
                if(zxChange.getExtend3() != null){
                    zxone.setLocation(Integer.parseInt(zxChange.getExtend3()));  //存放地点
                }
                SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
                Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
                //zxone.setCampus(new Long(schoolId).intValue());   //使用部门

                long l = SnowFlake.nextId();
                zxChange.setId(l);
                zxChange.setAssetsId(Long.parseLong(assetId));  //主表id（资产表）
                zxChange.setChangeType(6);   //6：部门领用
                zxChange.setExtend5(schoolId);
                zxChange.setExtend1(DateString.getString(new Date(),"yyyy-MM-dd HH:mm:ss"));  //创建时间

                zxChangeService.insertZxChange(zxChange);
                i1 = zxAssetManagementService.updateZxAssetManagement(zxone);
            }
            //session.removeAttribute("s");//清空session信息
            return toAjax(i1);
        }
        //session.removeAttribute("s");//清空session信息
        return toAjax(zxChangeService.insertZxChange(null));
    }

    /**
     * 部门领用详情
     */
    @GetMapping("/detail/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxReturnService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/detail";
    }

    /**
     * 弹框列表（调用页面）
     */
    @GetMapping("/assetList")
    public String assetList(){
        return prefix+"/list";
    }

    /**
     * 资产列表查询(弹框列表)
     */
    @PostMapping("/listooo")
    @ResponseBody
    public TableDataInfo listooo(ZxAssetManagement zxAssetManagement)
    {
        int campus = ShiroUtils.getSysUser().getDeptId().intValue();
        if (campus != 100){
            zxAssetManagement.setCampus(campus);
        }
        zxAssetManagement.setState(1);
        zxAssetManagement.setExtend3("0");  //未保修的
        startPage();
        List<ZxAssetManagement> list = zxAssetManagementService.selectAssetManagementLists(zxAssetManagement);
        return getDataTable(list);
    }

    @RequiresPermissions("property:department:listsan")
    @PostMapping("/listsan")
    @ResponseBody
    public TableDataInfo listsan(ZxAssetManagement zxAssetManagement, HttpServletRequest request)
    {
        zxAssetManagement.setExtend2(null);
        int num = zxAssetManagement.getNum(); //获取num（用于删除做判断：num=0删除状态,num=-1正常添加状态）
        if(num == 0){
            request.getSession().removeAttribute("s");//清空session信息

            if (zxAssetManagement.getIds() != null && !zxAssetManagement.getIds().equals("")){
                List<ZxAssetManagement> list = new LinkedList<>();
                String s = zxAssetManagement.getIds(); //获取ids
                HttpSession session = request.getSession();
                if(session.getAttribute("s") == null){
                    session.setAttribute("s","0");
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }else{
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }
                String spl = session.getAttribute("s").toString();
                Set set = new HashSet();
                String[] split = spl.split(",");
                for (int i=0; i<split.length; i++){
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
                List<ZxAssetManagement> list = new LinkedList<>();
                return getDataTable(list);
            }
        }else {
            if (zxAssetManagement.getIds() != null && !zxAssetManagement.getIds().equals("")){
                List<ZxAssetManagement> list = new LinkedList<>();
                String s = zxAssetManagement.getIds();
                HttpSession session = request.getSession();
                if(session.getAttribute("s") == null){
                    session.setAttribute("s","0");
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }else{
                    session.setAttribute("s",session.getAttribute("s")+","+s);
                }
                String spl = session.getAttribute("s").toString();
                Set set = new HashSet();
                String[] split = spl.split(",");
                for (int i=0; i<split.length; i++){
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
                List<ZxAssetManagement> list = new LinkedList<>();
                return getDataTable(list);
            }
        }

       /*if (zxAssetManagement.getIds() != null && !zxAssetManagement.getIds().equals("")){
           List<ZxAssetManagement> list = new LinkedList<>();
           String s = zxAssetManagement.getIds();
           String[] split = s.split(",");
           String ids = (String)httpSession.getAttribute("ids");
           if (ids != null && !ids.equals("")){
               String s1 = ids + s;
               String[] split1 = s1.split(",");
               List<String> list1=new ArrayList<>();
               for (int i=0;i<split1.length;i++){
               //如果集合里面没有相同的元素才往里存
                   if(!list1.contains(split1[i])){
                       list1.add(split1[i]);
                   }
               }
               split = list1.toArray(new String[1]);
           }else {
               httpSession.setAttribute("ids",s);
           }
           for (int i=0;i<split.length;i++){
               if (split[i] != null && !split[i].equals("")) {
                   ZxAssetManagement ls = zxAssetManagementService.selectZxAssetManagementById(Long.parseLong(split[i]));
                   list.add(ls);
               }
           }
           return getDataTable(list);
       }else {
           List<ZxAssetManagement> list=new LinkedList<>();
           return getDataTable(list);
       }*/
    }
}

