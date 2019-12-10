package com.hp.web.controller.property;

import com.hp.common.annotation.Log;
import com.hp.common.core.controller.BaseController;
import com.hp.common.core.domain.AjaxResult;
import com.hp.common.core.page.TableDataInfo;
import com.hp.common.enums.BusinessType;
import com.hp.common.utils.SnowFlake;
import com.hp.framework.util.ShiroUtils;
import com.hp.property.domain.ZxAssetManagement;
import com.hp.property.domain.ZxChange;
import com.hp.property.service.IZxAssetManagementService;
import com.hp.property.service.IZxChangeService;
import com.hp.property.service.IZxRepairsService;
import com.hp.system.domain.SysDept;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产报修控制层
 */
@Controller
@RequestMapping("/property/repairs")
public class ZxRepairsController extends BaseController {
    private String prefix = "property/repairs";

    @Autowired
    private IZxRepairsService zxRepairsService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysDictDataService iSysDictDataService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 页面展示
     * @return
     */
    @RequiresPermissions("property:repairs:view")
    @GetMapping()
    public String repairs(ModelMap mmap){
        List<SysDept> sysDepts = sysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        return prefix + "/repairs";
    }

    /**
     * 报修数据展示
     * @param zxChange
     * @return
     */

    @RequiresPermissions("property:repairs:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange){
        Long campus = ShiroUtils.getSysUser().getDeptId();
        if (campus != 100){
            zxChange.setExtend5(campus);
        }
        startPage();
        List<ZxChange> list = zxRepairsService.selectRepairsList(zxChange);
        return getDataTable(list);
    }

    /**
     * 资产报修详情页
     */
    @GetMapping("/select/{id}")
    public String select(@PathVariable("id") Long id, ModelMap mmap)
    {
        ZxAssetManagement zxAssetManagement = zxRepairsService.selectZxAssetManagementById(id);
        mmap.put("zxAssetManagement", zxAssetManagement);
        return prefix + "/select";
    }

    /**
     * 更改状态
     */
    @RequiresPermissions("property:repairs:remove")
    @Log(title = "更改状态", businessType = BusinessType.DELETE)
    @PostMapping("/xgstate")
    @ResponseBody
    public AjaxResult remove(Long id) {
        ZxAssetManagement management = zxRepairsService.selectZxAssetManagementById(id);
        String extend3 = management.getExtend3();
        int i;
        if(extend3.equals("1")){
            management.setExtend3("0");
            i= zxRepairsService.updateExtend3(management);
        }else if(extend3.equals("0")){
            management.setExtend3("1");
            i=zxRepairsService.updateExtend3(management);
        }else{
            i=0;
        }
        return toAjax(i);
    }

    /**
     * 新增报修页面展示
     * @return
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request)
    {
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        return prefix + "/add";
    }

    /**
     * 新增报修信息
     */
    @RequiresPermissions("property:repairs:add")
    @Log(title = "报修登记", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange,ZxAssetManagement zxAssetManagement,HttpServletRequest request)
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
                    zxone.setExtend3("1");  //保修状态,1:保修，0：未保修

                    long l = SnowFlake.nextId();
                    zxChange.setId(l);
                    zxChange.setAssetsId(Long.parseLong(s1));
                    zxChange.setChangeType(3);  //3：报修
                    //zxChange.setSubmittedDepartment(zxChange.getSubmittedDepartment());  //报修部门

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


    /**
     * 调用弹框页面
     * @return
     */
    @RequiresPermissions("property:return:adds")
    @GetMapping("/adds")
    public String adds()
    {
        return prefix + "/adds";
    }


    /**
     * 获取未报废状态的资产列表(弹框)
     * @return
     */
    @RequiresPermissions("property:return:list")
    @PostMapping("/listss")
    @ResponseBody
    public TableDataInfo listss(ZxAssetManagement zxAssetManagement)
    {
        int campus = ShiroUtils.getSysUser().getDeptId().intValue();
        if (campus != 100){
            zxAssetManagement.setCampus(campus);
        }
        startPage();
        List<ZxAssetManagement> list = zxRepairsService.selectAssetManagementList(zxAssetManagement);
        return getDataTable(list);
    }


    @RequiresPermissions("property:return:list")
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
}
