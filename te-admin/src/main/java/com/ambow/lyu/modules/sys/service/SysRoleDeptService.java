package com.ambow.lyu.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ambow.lyu.modules.sys.entity.SysRoleDeptEntity;

import java.util.List;


/**
 * 角色与部门对应关系
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
	
	void saveOrUpdate(Long roleId, List<Long> deptIdList);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long[] roleIds) ;

	/**
	 * 根据角色ID数组，批量删除
	 */
	int deleteBatch(Long[] roleIds);
}
