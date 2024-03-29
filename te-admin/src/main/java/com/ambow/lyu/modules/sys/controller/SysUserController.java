package com.ambow.lyu.modules.sys.controller;


import com.ambow.lyu.common.annotation.SysLog;
import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.common.validator.Assert;
import com.ambow.lyu.common.validator.ValidatorUtils;
import com.ambow.lyu.common.validator.group.AddGroup;
import com.ambow.lyu.common.validator.group.UpdateGroup;
import com.ambow.lyu.common.vo.Response;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.service.SysUserRoleService;
import com.ambow.lyu.modules.sys.service.SysUserService;
import com.ambow.lyu.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public Response list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);

        return Response.ok().put("page", page);
    }

    /**
     * 查询当前部门以及子部门人员根据姓名模糊查找
     */
    @RequestMapping("/list/{deptId}")
    public Response listByDept(@PathVariable("deptId") Long deptId) {
        List<SysUserEntity> list = sysUserService.queryByDept(deptId);
        return Response.ok().put("list", list);
    }


    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public Response info() {
        return Response.ok().put("user", getUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @RequestMapping("/password")
    public Response password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return Response.error("原密码不正确");
        }

        return Response.ok();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/info/{userId}")
    @RequiresPermissions("sys:user:info")
    public Response info(@PathVariable("userId") Long userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return Response.ok().put("user", user);
    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public Response save(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, AddGroup.class);

        sysUserService.saveUser(user);

        return Response.ok();
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public Response update(@RequestBody SysUserEntity user) {
        ValidatorUtils.validateEntity(user, UpdateGroup.class);

        sysUserService.update(user);

        return Response.ok();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Response delete(@RequestBody Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return Response.error("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return Response.error("当前用户不能删除");
        }

        sysUserService.removeByIds(Arrays.asList(userIds));

        return Response.ok();
    }
}
