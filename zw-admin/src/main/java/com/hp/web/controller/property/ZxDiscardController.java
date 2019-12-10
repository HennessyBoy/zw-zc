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
import com.hp.property.service.IZxDiscardService;
import com.hp.system.domain.SysDept;
import com.hp.system.domain.SysDictData;
import com.hp.system.domain.SysUser;
import com.hp.system.service.ISysDeptService;
import com.hp.system.service.ISysDictDataService;
import com.hp.system.service.ISysUserService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 资产报废Controller
 */
@Controller
@RequestMapping("/property/discard")
public class ZxDiscardController extends BaseController {
    private String prefix = "property/discard";

    @Autowired
    private IZxDiscardService zxDiscardService;

    @Autowired
    private IZxAssetManagementService zxAssetManagementService;

    @Autowired
    private IZxChangeService zxChangeService;

    @Autowired
    private ISysDeptService sysDeptService;

    @Autowired
    private ISysUserService iSysUserService;

    /**
     * 页面展示
     * @return
     */
    @RequiresPermissions("property:discard:view")
    @GetMapping()
    public String change(ModelMap mmap){
        List<SysDept> sysDepts = sysDeptService.selectDeptByParentId();
        mmap.put("school",sysDepts);
        return prefix + "/discard";
    }

    /**
     * 数据展示(查询报废列表)
     * @param
     * @return
     */
    @RequiresPermissions("property:discard:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ZxChange zxChange) {
        Long campus = ShiroUtils.getSysUser().getDeptId();
        if (campus != 100){
            zxChange.setExtend5(campus);
        }
        startPage();
        List<ZxChange> list = zxDiscardService.selectDiscardList(zxChange);
        return getDataTable(list);
    }

    /**
     * 新增报废信息
     */
    @GetMapping("/add")
    public String add(HttpServletRequest request) {
        if(request.getSession().getAttribute("s")!=null){
            request.getSession().removeAttribute("s");
        }
        return prefix + "/add";
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
    @RequiresPermissions("property:discard:alist")
    @PostMapping("/alist")
    @ResponseBody
    public TableDataInfo alist(ZxAssetManagement zxAssetManagement, HttpServletRequest request) {
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
     * 新增保存资产变更
     */
    @RequiresPermissions("property:discard:add")
    @Log(title = "新增资产报废", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ZxChange zxChange, HttpServletRequest request) {
        String ids =  request.getSession().getAttribute("s").toString();  //列表id
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
            for (Object id : set) {
                    String s1 = id.toString();  //获取单个id
                    if (!s1.equals("")) {
                        zxone = new ZxAssetManagement();  //创建ZxAssetManagement表对象（用于传参）
                        zxone.setId(Long.parseLong(s1));  //单个id
                        zxone.setState(3);  //状态（1：闲置，2：在用，3：报废）
                        zxone.setExtend1("");  //使用部门
                        zxone.setExtend2("");  //使用人
                        if(zxChange.getExtend3() != null){
                            zxone.setLocation(-1);  //存放地点
                        }
                        //zxone.setCampus(-1);   //使用部门(清除部门)

                        long l = SnowFlake.nextId();
                        zxChange.setId(l);
                        zxChange.setAssetsId(Long.parseLong(s1));
                        zxChange.setChangeType(4);  //7：报废

                        SysUser sysUser = ShiroUtils.getSysUser();  //获取用户信息
                        Long schoolId = sysUser.getDeptId();  //获取部门编号（部门）
                        zxChange.setExtend5(schoolId);
                        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        zxChange.setExtend1(time.format(new Date()));  //创建时间

                        int result = zxChangeService.insertZxChange(zxChange);  //插入报废新的记录
                        i1 = zxAssetManagementService.updateZxAssetManagement(zxone);  //更改报废后物品状态
                    }
                }
                return toAjax(i1);
            }else{
                return toAjax(zxChangeService.insertZxChange(null));
            }
        }

        /**
         * 查询资产信息列表（查询未报废的资产）
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
            List<ZxAssetManagement> list = zxDiscardService.selectZxNoDiscardList(zxAssetManagement);
            return getDataTable(list);
        }

        /**
         * 根据变更记录id查询详情
         */
        @GetMapping("/one/{id}")
        public String one (@PathVariable("id") Long id, ModelMap mmap)
        {
            ZxAssetManagement zxAssetManagement = zxDiscardService.selectZxAssetManagementById(id);
            mmap.put("zxAssetManagement", zxAssetManagement);
            return prefix + "/one";
        }




}
