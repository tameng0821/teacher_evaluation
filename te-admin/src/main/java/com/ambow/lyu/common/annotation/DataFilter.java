package com.ambow.lyu.common.annotation;

import java.lang.annotation.*;

/**
 * 数据过滤
 *
 * @author Mark sunlightcs@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataFilter {
    /**
     * 表的别名
     */
    String tableAlias() default "";

    /**
     * true：拥有用户角色对应的部门列表数据权限
     */
    boolean roleDept() default false;

    /**
     * true：拥有用户所属部门以及子部门数据权限
     */
    boolean userDept() default true;

    /**
     * true：没有本部门数据权限，也能查询本人数据
     */
    boolean user() default false;

    /**
     * 部门ID
     */
    String deptId() default "dept_id";

    /**
     * 用户ID
     */
    String userId() default "user_id";
}

