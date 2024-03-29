package com.ambow.lyu.modules.sys.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    /**
     * 保存用户
     */
    void saveUser(SysUserEntity user);

    /**
     * 修改用户
     */
    void update(SysUserEntity user);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param password    原密码
     * @param newPassword 新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    /**
     * 查询当前部门以及子部门的所有人员
     *
     * @param deptId 部门ID
     * @return 人员列表
     */
    List<SysUserEntity> queryByDept(Long deptId);

    /**
     * 统计当前部门以及子部门人数
     * @param deptId 部门ID
     * @return 人员列表
     */
    int countByDept(Long deptId);
}
