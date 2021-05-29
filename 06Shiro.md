Shiro
1.1 什么是Shiro
·Apache Shiro是一个java安全权限的框架
·可以在Java SE和 JavaEE容易的开发出足够好的应用
·Shiro可以完成[认证、授权、加密、会话管理、web集成、缓存等]
·下载地址 http://shiro.apache.org/
 github地址: github.com/apache/shiro
 
1.2快速开始
· pom.xml中导入依赖
· 编写配置文件shiro.ini
· Quickstart.java
2020-02-03 16:37:42,530 INFO [org.apache.shiro.session.mgt.AbstractValidatingSessionManager] - Enabling session validation scheduler... 
2020-02-03 16:37:42,820 INFO [Quickstart] - Retrieved the correct value! [aValue] 
2020-02-03 16:37:42,823 INFO [Quickstart] - User [lonestarr] logged in successfully. 
2020-02-03 16:37:42,823 INFO [Quickstart] - May the Schwartz be with you! 
2020-02-03 16:37:42,824 INFO [Quickstart] - You may use a lightsaber ring.  Use it wisely. 
2020-02-03 16:37:42,824 INFO [Quickstart] - You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  Here are the keys - have fun! 
===>成功打印出日志信息
·注意点:pom.xml文件中导入依赖的时候 日志门面scope设置成runtime test会报错
### 默认使用的日志门面 commos-logging(需要在pom.xml中导入相应的包才行)
1.3开始读代码和配置Quickstart.java=====[Subject对象的大部分方法-----类似SpringSecurity]
//1.获得当前执行用户对象Subject:###########################################
Subject currentUser = SecurityUtils.getSubject();
//2.通过当前用户使用shiro的session#(而不是http session)############
Session session = currentUser.getSession();
//3.登录当前用户 角色 和 权限 是否被认证!###########################################
currentUser.isAuthenticated()
//4.输出身份信息 (一个用户名): 
currentUser.getPrincipal()
//5.测试当前用户是否有某个角色:
currentUser.hasRole("schwartz")
//6.测试用户是否有以下权限 (非实例级) 是否有lightsabe:wield权限  [粗粒度-权限更多]
currentUser.isPermitted("lightsaber:wield")
//7.测试用户是否有winnebago:drive:eagle5权限  [细粒度-权限更少]
currentUser.isPermitted("winnebago:drive:eagle5")
//8.结束退出!
currentUser.logout();

2 shiro整合mybatis
