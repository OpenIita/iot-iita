# 消息中心

## 数据库设计

### 通道类型配置

~~~mysql
CREATE TABLE channel_type
(
    id         bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    title      varchar(128) NOT NULL COMMENT '标题',
    createTime timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT '通道类型';

INSERT INTO channel_type(title, createTime, updateTime)
VALUES ('邮箱', current_timestamp(), current_timestamp());
INSERT INTO channel_type(title, createTime, updateTime)
VALUES ('飞书', current_timestamp(), current_timestamp());
INSERT INTO channel_type(title, createTime, updateTime)
VALUES ('短信', current_timestamp(), current_timestamp());
INSERT INTO channel_type(title, createTime, updateTime)
VALUES ('钉钉', current_timestamp(), current_timestamp());
INSERT INTO channel_type(title, createTime, updateTime)
VALUES ('企业微信', current_timestamp(), current_timestamp());

CREATE TABLE channel
(
    id          bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    title       varchar(128) COMMENT '标题',
    channelType bigint NOT NULL COMMENT '渠道类型',
    configParam text   NOT NULL COMMENT '配置参数',
    userId      bigint COMMENT '配置归属',
    createTime  timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime  timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT '通道';


CREATE TABLE message
(
    id         bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    mac        varchar(128) COMMENT '设备MAC',
    deviceId   varchar(128) COMMENT '设备ID',
    content    text NOT NULL COMMENT '消息内容',
    createTime timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间'
) COMMENT '消息';




~~~

### 通道类型配置

~~~mysql
CREATE TABLE channel
(
    id          bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    title       varchar(128) COMMENT '类型',
    configParam text COMMENT '配置参数',
    createTime  datetime COMMENT '创建时间',
    updateTime  datetime COMMENT '修改时间'
) COMMENT '渠道';
~~~

### 通道订阅

~~~mysql
CREATE TABLE channel_subscribe
(
    id         bigint PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    channelId  bigint COMMENT '通道',
    userId     bigint COMMENT '用户ID',
    createTime datetime COMMENT '创建时间',
    updateTime datetime COMMENT '修改时间'
) COMMENT '渠道';
~~~