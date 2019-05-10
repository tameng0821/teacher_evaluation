# 学院教师评价系统

#### 介绍
根据《教师教学质量评价实施方案（试行）》开发教师评价系统。

#### 软件架构
1. 使用人人开源社区项目renren_security为基础开发。
2. 技术关键词：Java、Spring、SpringBoot、Mybatis、Mybatis-plus、shiro。

#### 安装教程

1. 根据情形修改数据库连接地址、配置文件内的端口、ContextPath，打包te-admin.jar。
2. 使用setsid java -jar te-admin.jar &，后台运行项目。

#### 使用说明

1. 环境要求 JDK1.8

#### 参与贡献
1. 默认分支为dev分支（常规分支）。对项目感兴趣欢迎fork。项目参与人员使用如下命令更新/提交。
2. 创建本地仓库。
    1. mkdir teacher_evaluation
    2. git init
    3. git remote add gitee git@gitee.com:lyu_info_chuangke/teacher_evaluation.git -t dev
    4. git pull gitee dev
3. 提交代码
    1. git add .
    2. git commit -m "第一次提交"
    3. git push gitee dev
4. 管理员新建 Pull Request，将dev分支代码合并到master分支（保护分支）。


#### 码云技术支持

1.  码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)