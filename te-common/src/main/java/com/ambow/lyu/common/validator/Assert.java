package com.ambow.lyu.common.validator;

import com.ambow.lyu.common.exception.LteException;
import org.apache.commons.lang.StringUtils;

/**
 * 数据校验
 *
 * @author Mark sunlightcs@gmail.com
 */
public abstract class Assert {

    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new LteException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new LteException(message);
        }
    }
}
