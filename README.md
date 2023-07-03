

<p align="center">
<img alt="logo" src="https://xiwasong.github.io/img/logo.png" width="100">
</p>
<p align="center"><img  src="https://img.shields.io/badge/JDK-11+-important.svg">  <img  src="https://img.shields.io/badge/Redis-5.0-important.svg"> <img  src="https://img.shields.io/badge/SpringBoot-2.6.2-blue.svg"> <img  src="https://img.shields.io/badge/Elasticsearch-7.15.2-red.svg">  <img  src="https://img.shields.io/badge/Satoken-1.30.0-yellow.svg"> 
</p>
<p align="center"><img  src="https://img.shields.io/badge/License-Apache2.0-lightgrey.svg"> 
<img  src="https://img.shields.io/badge/Copyright-铱塔智联/openiita-60569f.svg"> <img  src="https://img.shields.io/badge/v0.4.5-60569f.svg">
<img  src="https://img.shields.io/badge/物联网平台-f44e91.svg">
</p>

#### 介绍
此仓库为铱塔智联(openiita)物联网平台开源项目。
铱塔智联开源平台是一个开源的物联网基础开发平台，提供了物联网及相关业务开发的常见基础功能, 能帮助你快速搭建自己的物联网相关业务平台。

系统包含了品类、物模型、消息转换、通讯组件（mqtt/EMQX通讯组件、小度音箱接入组件、onenet Studio接入组件）、modbus透传接入、云端低代码设备开发、设备管理、设备分组、规则引擎、第三方平台接入、数据流转（http/mqtt/kafka）、数据可视化、报警中心等模块和智能家居APP（小程序）。

#### 低配服务器万级设备接入能力

服务器配置： 1台2核2G(阿里云突发性能实例)跑主程序、1台2核4G(阿里云突发性能实例)跑TDengine和RocketMq

程序配置：使用内置H2数据库和内置mqtt

设备接入情况：接入4千台网关+8千台子设备，其中4千台子设备每10秒上报一条数据

稳定运行后：设备在线并正常上报和下发、CPU占用未超过50%、界面操作没有卡顿

#### 低学习门槛

1、零配置一键运行，小白也会用

2、低环境要求，不需要额外安装软件

3、平民级代码，没有形式化的分层和过度封装，简单易懂


#### 主流数据库支持

关系型数据库：理论上支持所有实现了标准SQL的数据库，如： DB2、H2、HSQLDB、MariaDB、Microsoft SQL Server、MySQL、Oracle、Postgres

时序数据库：elasticsearch、TDEngine

K-V数据库：redis

#### 消息总线支持

Vertx event-bus（内置）、RocketMQ，通过扩展模块可接入其它任意消息中间件。


#### 系统生态

**前端项目见：** https://gitee.com/openiita/iita-iot-web-admin

 **演示地址：** [点击访问](http://159.75.222.119:16808)


**系统截图**

![输入图片说明](doc/screenshot.png) 



#### 软件架构
软件架构说明
本系统采用springboot、jpa、vertx、redis、H2、mysql、elasticsearch、TDEngine、RocketMq、sa-token等框架和第三方软件，中间件采用模块化无侵入集成非常容易扩展和替换。

默认使用内置的redis、h2数据库、es时序数据库和vertx消息总线以减小启动难度，可实现 **零配置一键启动** ，程序在第一次启动的时候会使用data/init目录的示例数据进行初始化。

管理员账号密码：admin/admin123


**注：** es版本为7.15.2，mysql版本为8.0+

##### 关系数据库切换为mysql方法（其它数据库同理）

1、将iot-data-serviceImpl-rdb/pom.xml中的mysql驱动注释放开

2、启动时指定active: --spring.profiles.active=mysql

##### 时序数据库切换为TDengein方法

1、注释掉iot-standalone/pom.xml中的 iot-temporal-serviceImpl-es，并打开iot-td-temporal-service的注释

2、application.xml中注释掉elasticsearch配置，并打开td-datasource配置

##### 消息总线切换为RocketMq方法

1、注释掉iot-starter/pom.xml中的 iot-message-event-bus，并打开iot-message-rocketmq的注释

2、application.xml中打开rocketmq配置


#### 运行步骤
1、安装jdk11

2、clone代码，在idea中导入项目

3、在iot-starter模块的Application类上右键运行

4、若出现报错，可能是数据没有初始化完或重复初始化，把项目根目录下.init文件和data/iotkit.xx文件删除后重启


#### 服务器部署

执行maven打包在iot-standalone模块中将打包后的iot-standalone.xx.zip上传到服务器，解压后进入bin目录，执行start.sh或start.bat启动。


#### 文档

http://iotkit-open-source.gitee.io/document/



#### 捐助与支持
如果您觉得我的开源软件对你有所帮助请关注、star、fork :kissing_heart: 。

如果我的开源软件应用到您的商业项目中，请务必通知到我，因为得到用户的认可是支撑开源的动力。

交流QQ群: 940575749

微信群(添加我微信备注"进群"):

![输入图片说明](doc/ma.png) 


### 项目规划

见：《[开源项目管理](https://open-iita.feishu.cn/base/G4owbWqkbaoI0LsYVnWcreXVnnc)》

![输入图片说明](https://foruda.gitee.com/images/1686571717085038365/f1bd636b_504690.png "proj.png")

