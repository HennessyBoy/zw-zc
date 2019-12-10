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
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IzxTransferService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产变更Controller
 *
 * @author hp
 * @date 2019-09-02
 */
@Controller
@RequestMapping("/property/transfer")
public class ZxChangeController extends BaseController
{
    private String prefix = "property/transfer";

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private IzxTransferService zxTransferService;

    @Autowired
    private ISysDeptService sysDeptService;

    @RequiresPermissions("property:transfer:view")
    @GetMapping()
    public String change(ModelMap mmap){
        List<SysDept> sysDepts = sysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        /*//部门
        List<SysDept> sysDepts = sysDeptService.selectSchoolByParentId();
        mmap.put("school",sysDepts);*/
        return prefix + "/transfer";
    }

    /**
     *@description:  转移查询
     *@param:
     *
     */
    @RequiresPermissions("property:transfer:transferList")
    @PostMapping("/transferList")
    @ResponseBody
    public TableDataInfo transferList(ZxChange zxChange)
    {
        Long campus = ShiroUtils.getSysUser().getDeptId();
        if (campus != 100){
            zxChange.setExtend5(campus);
        }
        startPage();
        List<ZxChange> list = zxTransferService.selectTransferList(zxChange);
        return getDataTable(list);
    }


    /**
     * 根据变更记录id查询详情
     */
    @GetMapping("/one/{id}")
    public String one (@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxTransferService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/one";
    }


    /**
     * 新增报废信息
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request,ModelMap mmap) {
        //清空session
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        SysDept dept = new SysDept();
        SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
        Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
        List<SysDept> deptList = null;
        if(schoolId == 100){
            deptList = sysDeptService.selectSchoolByParentId();
        }else {
            dept.setParentId(schoolId);
            deptList = sysDeptService.selectDeptList(dept);
        }
        mmap.put("deptList", deptList);
        return prefix + "/add";
    }

    /**
     * 用于部门与存放地点的级联
     * @param parentId
     * @return
     */
    @PostMapping("/selectPlaceBySchoolId")
    @ResponseBody
    public List<SysDept> selectPlaceBySchoolId(Long parentId)
    {
        //存放地点
        List<SysDept> sysDepts = sysDeptService.selectPlaceByParentId(parentId);
        return sysDepts;
    }

    /**
     * 跳转到查询未报废的信息列表的页面(子窗口)
     */
    @GetMapping("/insert")
    public String insert() {
        return prefix + "/insert";
    }

    /**
     * 根据小窗口的id查未报废的资产数据
     * <p>
     * （就是根据传过来的ids查询数据）
     *
     * @param zxAssetManagement
     * @return
     */
    @PostMapping("/alist")
    @ResponseBody
    public TableDataInfo alist(ZxAssetManagement zxAssetManagement, HttpServletRequest request) {
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
            if (zxAssetManagement.getIds() != null && !zxAssetManagement.getIds().equals("")) {
                List<ZxAssetManagement> list = new LinkedList<>();
                //获取id
                String s = zxAssetManagement.getIds();
                System.out.println("s=" + s);
                //获取session
                HttpSession session = request.getSession();
                //判断session是否为空
                if (session.getAttribute("s") == null) {
                    session.setAttribute("s", "0");//为了保持一样的格式方便切割（0，s）
                    session.setAttribute("s", session.getAttribute("s") + "," + s);
                } else {
                    session.setAttribute("s", session.getAttribute("s") + "," + s);
                }
                //获取session里的s的值
                String spl = session.getAttribute("s").toString();
                Set set = new HashSet();
                //切割
                String[] split = spl.split(",");
                //利用set去除重复的id
                for (int i = 0; i < split.length; i++) {
                    set.add(split[i]);
                }
                //移除0和""
                set.remove("0");
                set.remove("");
                //根据id查询相应的资产，存入list
                for (Object id : set) {
                    String s1 = id.toString();
                    if (!s1.equals("")) {
                        zxAssetManagement.setId(Long.parseLong(s1));
                        ZxAssetManagement ls = zxAssetManagementService.selectAssetManagementListById(zxAssetManagement);
                        list.add(ls);
                    }
                }
                return getDataTable(list);
            } else {
                List<ZxAssetManagement> list = new LinkedList<>();
                return getDataTable(list);
            }
        }
    }


    /**
     * 新增保存资产转移
     */
    @RequiresPermissions("property:transfer:add")
    @Log(title = "新增资产转移", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange, HttpServletRequest request) {
        String ids =  request.getSession().getAttribute("s").toString();  //列表id
        //String ids = zxAssetManagement.getIds();
        int i1=0;
        if (ids != null && !ids.equals("")) {
            ZxAssetManagement zxone = null;
            Set set = new HashSet();
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                set.add(split[i]);
            }
            set.remove("0");
            set.remove("");
            System.out.println("set=" + set);
            for (Object id : set) {
                String s1 = id.toString();  //获取单个id
                if (!s1.equals("")) {
                    zxone = new ZxAssetManagement();  //创建ZxAssetManagement表对象（用于传参）
                    zxone.setId(Long.parseLong(s1));  //单个id
                    //zxone.setState(3);  //状态（1：闲置，2：在用，3：报废）
                    if(zxChange.getUseDepartment() != null){
                        zxone.setExtend1(zxChange.getUseDepartment().toString());  //使用部门
                    }
                    if(zxChange.getExtend5() != null){
                        zxone.setCampus(new Long(zxChange.getExtend5()).intValue());   //使用部门
                    }
                    zxone.setExtend2(zxChange.getUsers());  //使用人
                    if(zxChange.getExtend3() != null){
                        zxone.setLocation(Integer.parseInt(zxChange.getExtend3()));  //存放地点
                    }

                    SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
                    Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
                    long l = SnowFlake.nextId();
                    zxChange.setId(l);
                    zxChange.setAssetsId(Long.parseLong(s1));
                    zxChange.setChangeType(2);  //2：转移
                    zxChange.setExtend5(schoolId);
                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    zxChange.setExtend1(time.format(new Date()));  //创建时间

                    int result = zxChangeService.insertZxChange(zxChange);  //插入转移新的记录
                    i1 = zxAssetManagementService.updateZxAssetManagement(zxone);  //更改转移后物品状态
                }
            }
            return toAjax(i1);
        }else{
            return toAjax(zxChangeService.insertZxChange(null));
        }
    }

    /**
     * 查询资产信息列表（查询未转移的资产）
     */
    @PostMapping("/inserts")
    @ResponseBody
    public TableDataInfo insert (ZxAssetManagement zxAssetManagement)
    {
        int campus = ShiroUtils.getSysUser().getDeptId().intValue();
        if (campus != 100){
            zxAssetManagement.setCampus(campus);
        }
        startPage();
        List<ZxAssetManagement> list = zxTransferService.selectNoTransferList(zxAssetManagement);
        return getDataTable(list);
    }



}
