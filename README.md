<p align="center">
	<img src="" alt="" style="zoom:50%;" align="center" />
</p>
<p align=center>ToLearn一起来学-用户匹配系统</p>
<p align="center">
	<a target="_blank" href="https://github.com/BraumAce/ToLearn-backend"></a>
    	<img src="https://img.shields.io/badge/license-Apache%202.0-blue"></img>
    	<img src="https://img.shields.io/badge/JDK-17-green"></img>
	<img src="https://img.shields.io/badge/SpringBoot-2.7.5-green"></img>
        <img src="https://img.shields.io/badge/Node-20.12.2-green"></img>
        <img src="https://img.shields.io/badge/MySQL-8.0-green"></img>
	<img src="https://img.shields.io/badge/MyBatis-2.2.2-green"></img>
	<img src="https://img.shields.io/badge/MyBatis%20Plus-3.5.1-green"></img>
        <img src="https://img.shields.io/badge/Vue-3.4.21-green"></img>
	<img src="https://img.shields.io/badge/Vant-3.8.8-green"></img>
        <img src="https://img.shields.io/badge/vite-5.2.0-green"></img>		
        <img src="https://img.shields.io/badge/Easy%20Excel-3.1.0-green"></img>
	<img src="https://img.shields.io/badge/knife4j-4.4.0-green"></img>
	<img src="https://img.shields.io/badge/Gson-2.8.9-green"></img>
	<img src="https://img.shields.io/badge/Redis-7.0.2-green"></img>
	<img src="https://img.shields.io/badge/WebSocket-4.5.13-green"></img>
	<img src="https://img.shields.io/badge/Axios-1.6.8-green"></img>
</p>


## 项目介绍
一起来学(ToLearn)，一个前后端分离的用户匹配系统，前端使用 **Vite + Vue3 + Vant4**，后端使用 **SpringBoot 2.7** + **Mybatis-plus**，使用 **WebSocket** 实现实时通信，并结合 **阿里云SDK + 七牛云SDK** 完成短信发送和图片上传。本项目使用 Apache License Version 2.0 开源协议。
    
#### 前端地址:

[BraumAce/ToLearn-frontend (github.com)](https://github.com/BraumAce/ToLearn-frontend)

#### 后端地址:

[BraumAce/ToLearn-backend (github.com)](https://github.com/BraumAce/ToLearn-backend)

## 项目背景

寻找志同道合的小伙伴一起来学习吧！

## 核心功能

1. 用户注册和登录：用户可以通过注册账号并登录使用该网站。
2. 标签匹配：用户可以选择自己的技能和需求标签，系统会根据标签匹配合适的队友。
3. 组队功能：用户可以与其他用户组建队伍，一起组队学习。
4. 博文发布、点赞和关注：用户可以发布自己的博文，其他用户可以对其进行点赞和关注，以便更好地了解和交流。
5. 实时聊天：队伍中的用户可以进行实时聊天，方便沟通和协作。
6. 用户管理：管理员可以对用户进行管理，包括审核用户信息和处理用户投诉等。

## 项目亮点

1. 用户登录：使用 Redis 实现分布式 Session，解决集群间登录态同步问题；使用 token 储存用户信息并实现续签和超时自动退出；使用 Hash 代替 String 存储用户信息，节约了约 78.2% 的内存并便于单字段的修改。
2. 对于项目中复杂的集合处理（比如为队伍列表关联已加入队伍的用户），使用 Java 8 Stream API 和 Lambda 表达式来简化编码。
3. 使用 Redis 缓存首页高频访问的用户信息列表，将接口响应时长从 13380ms 缩短至 510ms。且通过自定义 Redis 序列化器来解决数据乱码、空间浪费的问题。
4. 为解决首次访问系统的用户主页加载过慢的问题，使用 Spring Scheduler 定时任务来实现缓存预热，并通过分布式锁保证多机部署时定时任务不会重复执行。
5. 为解决同一用户重复加入队伍、入队人数超限的问题，使用 Redisson 分布式锁来实现操作互斥，保证了接口幂等性。
6. 为快速判断用户是否在队伍中，使用布隆过滤器对记录进行去重，并解决缓存穿透的问题。
7. 使用编辑距离算法实现了根据标签匹配最相似用户的功能，并通过优先队列来减少 TOP N 运算过程中的内存占用。
8. 使用 Knife4j + Swagger 自动生成后端接口文档，并通过编写 ApiOperation 等注解补充接口注释，避免了人工编写维护文档的麻烦。
9. 使用本地+阿里云 OSS 储存用户头像，并自定义 CDN 加速域名指向项目专用储存空间。
10. 使用 WebSocket 在单个 TCP 连接上进行全双工通信，创建持久性的连接，实现队伍聊天室中的实时聊天。
11. 前端使用 Vant UI 组件库，并封装了全局通用的 Layout 组件，使主页、搜索页、组队页布局一致、并减少重复代码。
12. 基于 Vue Router 全局路由守卫实现了根据不同页面来动态切换导航栏标题， 并通过在全局路由配置文件扩展 title 字段来减少无意义的 if else 代码。
13. 使用 component is 标签自定义少数页面的基本布局，优化用户体验。
14. 封装前端卡片组件，对数据做统一处理，减少重复代码。
15. 使用 defineEmits 为子组件绑定事件，并使用 emits 将事件发送给父组件，优化用户在操作后的体验。

## 项目地址

目前项目仅托管 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Github地址： [https://github.com/BraumAce/ToLearn-backend](https://github.com/BraumAce/ToLearn-backend)

## 技术选型

**前端**

\- Vue 3

\- Vite 脚手架

\- Vant UI 移动端组件库

\- Axios 请求库



**后端**

\- Java SpringBoot 框架

\- MySQL 数据库

\- Mybatis-Plus

\- Mybatis X

\- Redis缓存

\- Redisson 分布式锁

\- Spring Scheduler 定时任务

\- Swagger + Knife4j 接口文档

\- Gson JSON序列化库

\- 最短编辑距离算法

\- WebSocket

## 后端部署

1. 执行安装后端所需依赖

   ```bash
   maven install
   ```

2. 修改 application.xml ，详细配置信息请见项目文档

3. 新建 to_learn数据库，并执行 to_learn.sql
4. 运行 ToLearnBackendApplication

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法，遇到一些 **BUG** 并修复了，欢迎小伙伴们提交 **Pull Request** 参与开源贡献！

1. **fork** 本项目到自己的 **repo**
2. 把 **fork** 过去的项目也就是你仓库中的项目 **clone** 到你的本地
3. 修改代码
4. **commit** 后 **push** 到自己的库
5. 发起**PR**（ **pull request**） 请求，提交到 **dev** 分支
6. 等待作者合并
