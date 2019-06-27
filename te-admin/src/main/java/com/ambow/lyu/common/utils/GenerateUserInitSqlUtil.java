package com.ambow.lyu.common.utils;

import com.ambow.lyu.modules.sys.entity.SysUserEntity;
import com.ambow.lyu.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author woondeewyy
 * @date 2019/6/27
 */
public class GenerateUserInitSqlUtil {
    /**
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('4', '3', '公共计算机教学部', '1', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('5', '3', '计算机工程', '2', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('6', '3', '教学办', '3', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('7', '3', '软件工程', '4', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('8', '3', '网络与信息工程', '5', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('9', '3', '实验中心', '6', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('10', '3', '通信工程', '7', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('11', '3', '团学办', '8', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('12', '3', '院办', '9', '0');
     * INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('13', '3', '其他', '10', '0');
     *
     * @param deptName
     * @return
     */
    public static int getDeptId(String deptName) {
        if (StringUtils.isBlank(deptName)) {
            return 13;
        }
        switch (deptName) {
            case "公共计算机教学部":
                return 4;
            case "计算机工程":
                return 5;
            case "教学办":
                return 6;
            case "软件工程":
                return 7;
            case "网络与信息工程":
                return 8;
            case "实验中心":
                return 9;
            case "通信工程":
                return 10;
            case "团学办":
                return 11;
            case "院办":
                return 12;
            default:
                return 13;

        }
    }

    public static void createInitSql(String filePath) throws IOException {
        File file = new File(filePath);
        List<Map<Integer, String>> content = ExcelUtils.readExcel(file, 0);
        Map<String, SysUserEntity> userEntityMap = new HashMap<>();
        for (int i = 1; i < content.size(); ++i) {
            SysUserEntity userEntity = new SysUserEntity();
            userEntity.setUsername(content.get(i).get(0));
            userEntity.setName(content.get(i).get(1));
            userEntity.setEmail(content.get(i).get(3));
            userEntity.setDeptName(content.get(i).get(4));
            userEntity.setDeptId((long) getDeptId(content.get(i).get(4)));
            userEntityMap.put(content.get(i).get(0),userEntity);
        }
        System.out.println("去除重复后，共计:"+userEntityMap.size());

        System.out.println("DELETE FROM sys_user_role WHERE role_id = 1;");
        System.out.println("DELETE FROM sys_user WHERE user_id >= 100;");
        int index = 100;
        for(SysUserEntity user : userEntityMap.values()){

            //以工号作为登录密码
            user.setPassword(user.getUsername());
            //sha256加密
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getSalt()));

            System.out.println("INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`,`name`,`email`, `mobile`, `status`, `dept_id`, `create_time`) " +
                    "VALUES ("+index+", '"+user.getUsername()+"', '"+user.getPassword()+"', '"+user.getSalt()+"', '"+user.getName()+"', " +
                    (user.getEmail()==null?"NULL":("'"+user.getEmail()+"'"))+
                    ", NULL, 1, "+user.getDeptId()+", '2019-06-27 12:00:00');");
            System.out.println("INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES ("+index+", "+index+", 1);");
            ++index;
        }
    }

    public static void main(String[] args) throws IOException {
        createInitSql("C:\\Users\\w\\Documents\\项目文档\\教师评价系统_项目文档目录\\01产品需求文档\\人员信息.xlsx");
    }

}
