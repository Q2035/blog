# 小而美的个人博客
采用Java编写，根据李仁密的小而美的个人博客系统开发而成

舍弃JPA，采用MyBatis作为持久层框架

框架以及软件版本：
- IDEA ULTIMATE 2019.3
- SpringBoot 2.2.2
- MyBatis 2.1.1
- Thymeleaf 3.0.11
- MySQL 8.0.19
- Redis 5.0.7
- Maven 3.6.3
- JDK 11.0.5

## 更新日志
2020.3.29更新：
    
    为了加快首页打开的速度，增加Redis将博客信息缓存，但是管理员博客加载速度依旧不理想，今天算了，下次再说
    :smirk:

2020.4.1更新：

    这次主要是升级了MySQL版本(5.6.47->8.0.19,MySQL-Connector-J 6.0.6->8.0.13)，修复博客点击之后观看
    次数不会变化的bug;顺便创建新用户操作数据库，防止手误出现删库的情况.
~~之后可能会把样式和脚本放在本地，万一人家的CDN失效了呢~~ 挺香的

2020.4.4更新：
    
    解决发表博客、修改Type、Tag之后首页未更新的问题，我咋就记得我修复过呀
    
2020.4.5更新：
    
    解决首页小工具无法展示的问题；
    解决新增博客后，首页type和tag没有更新的问题