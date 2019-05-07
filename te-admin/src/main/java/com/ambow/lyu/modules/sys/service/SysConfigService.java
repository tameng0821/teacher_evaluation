package com.ambow.lyu.modules.sys.service;

import com.ambow.lyu.common.utils.PageUtils;
import com.ambow.lyu.modules.sys.entity.SysConfigEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * 系统配置信息
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysConfigService extends IService<SysConfigEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存配置信息
     */
    void saveConfig(SysConfigEntity config);

    /**
     * 更新配置信息
     */
    void update(SysConfigEntity config);

    /**
     * 根据key，更新value
     */
    void updateValueByKey(String key, String value);

    /**
     * 删除配置信息
     */
    void deleteBatch(Long[] ids);

    /**
     * 根据key，获取配置的value值
     *
     * @param key key
     */
    String getValue(String key);

    /**
     * 根据key，获取value的Object对象
     *
     * @param key   key
     * @param clazz Object对象
     */
    <T> T getConfigObject(String key, Class<T> clazz);

}
