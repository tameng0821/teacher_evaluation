package com.ambow.lyu.modules.sys.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.sys.entity.SysDictEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 数据字典
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

