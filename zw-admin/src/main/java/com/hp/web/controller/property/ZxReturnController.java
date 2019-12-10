package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.core.text.Convert;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.DateString;
import com.hp.common.utils.FastJsonUtils;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxReturnService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产退还控制层
 */
@Controller
@RequestMapping("/property/return")
public class ZxReturnController extends BaseController {
    private String prefix = "property/return";

    @Autowired
    private IZxReturnService zxReturnService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private ISysDeptService sysDeptService;

    /**
     * 页面展示
     * @return
     */
    @RequiresPermissions("property:return:view")
    @GetMapping()
    public String returns(ModelMap mmap)
    {
        List<SysDept> sysDepts = sysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        return prefix + "/return";
    }

    /**
     * 数据展示(查询退还列表)
     * @param
     * @return
     */
    @RequiresPermissions("property:return:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange){
        Long campus = ShiroUtils.getSysUser().getDeptId();
        if (campus != 100){
            zxChange.setExtend5(campus);
        }
        startPage();
        List<ZxChange> list = zxReturnService.selectReturnList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增页面展示
     * @return
     */

    @RequiresPermissions("property:return:add")
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
     * 新增退还信息
     */
    @RequiresPermissions("property:return:add")
    @Log(title = "退还登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange,HttpServletRequest request)
    {
        String ids =  request.getSession().getAttribute("s").toString();  //列表id
        //String ids = zxAssetManagement.getIds();
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
                String s1 = id.toString();  //获取单个id
                if (!s1.equals("")) {
                    zxone = new ZxAssetManagement();  //创建ZxAssetManagement表对象（用于传参）
                    zxone.setId(Long.parseLong(s1));  //单个id
                    zxone.setState(1);  //状态（1：闲置，2：在用，3：报废）
                    if(zxChange.getSubmittedDepartment() != null){
                        zxone.setExtend1("");  //使用部门
                    }
                    zxone.setExtend2("");  //使用人
                    if(zxChange.getExtend3() != null){
                        zxone.setLocation(Integer.parseInt(zxChange.getExtend3()));  //存放地点
                    }
                    //zxone.setCampus(-1);   //使用部门(清除部门)

                    long l = SnowFlake.nextId();
                    zxChange.setId(l);
                    zxChange.setAssetsId(Long.parseLong(s1));
                    zxChange.setChangeType(7);  //7：退还

                    SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
                    Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
                    zxChange.setExtend5(schoolId);
                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    zxChange.setExtend1(time.format(new Date()));  //创建时间

                    int result = zxChangeService.insertZxChange(zxChange);  //插入退还新的记录
                    i1 = zxAssetManagementService.updateZxAssetManagement(zxone);  //更改退还后物品状态
                }
            }
            return toAjax(i1);
        }else{
            return toAjax(zxChangeService.insertZxChange(null));
        }
    }

    @GetMapping("/adds")
    public String adds()
    {
        return prefix + "/adds";
    }


    /**
     * 退还详情页面
     */
    @GetMapping("/select/{id}")
    public String select(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxReturnService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/select";
    }


    /**
     * 获取在用状态的列表(弹框)
     * @return
     */
    @PostMapping("/listss")
    @ResponseBody
    public TableDataInfo listss(ZxAssetManagement zxAssetManagement)
    {
        int campus = ShiroUtils.getSysUser().getDeptId().intValue();
        if (campus != 100){
            zxAssetManagement.setCampus(campus);
        }
        startPage();
        List<ZxAssetManagement> list = zxReturnService.selectAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }


    @PostMapping("/lists")
    @ResponseBody
    public TableDataInfo lists(ZxAssetManagement zxAssetManagement, HttpServletRequest request) {
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
    }

    /**
     * 删除
     * @param ids
     * @return
     *//*
    @RequiresPermissions("property:return:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String id,HttpServletRequest request) {
        if(!id.equals("")&&id!=null){
            System.out.println(id);
            String spl=request.getSession().getAttribute("s").toString();
            String[] split =spl.split(",");
            String sst="";
            for (int i=0;i<split.length;i++){
              if(split[i].equals(id)){
                  split[i]=split[i].replace(id,"");
              }
              sst=sst+","+split[i];

            }

            System.out.println(sst);
            request.getSession().setAttribute("s",sst);
            return toAjax(1);
        }else{
            return toAjax(0);
        }

    }
*/
}
