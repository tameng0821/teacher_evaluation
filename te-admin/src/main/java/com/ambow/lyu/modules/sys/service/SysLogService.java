package com.ambow.lyu.modules.sys.service;


import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.sys.entity.SysLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;


/**
 * 系统日志
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
