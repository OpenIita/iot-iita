# 品类表
CREATE TABLE IF NOT EXISTS category(
    id varchar(32) comment "品类id",
    `name` varchar(100) comment "品类名称",
    createAt TIMESTAMP comment "创建时间",
		PRIMARY KEY(id)
);

# 设备表
CREATE TABLE IF NOT EXISTS device_info(
    id varchar(32),
    deviceId varchar(32) COMMENT "设备ID",
    productKey varchar(32) COMMENT "产品key",
    deviceName varchar(32) COMMENT "设备唯一码",
    model varchar(32) COMMENT "设备型号",
    secret varchar(32) COMMENT "设备密钥",
    parentId varchar(32) COMMENT "父级设备ID",
    uid varchar(32) COMMENT "设备所属用户ID",
    createAt TIMESTAMP COMMENT "创建时间",
    INDEX idx_device_id (deviceId),
    INDEX idx_device_name (deviceName),
		PRIMARY KEY(id)
);

# 设备子用户表，一个设备可分配给多个子用户
CREATE TABLE IF NOT EXISTS device_sub_user(
    id varchar(32),
    deviceId varchar(32) COMMENT "设备ID",
    uid varchar(32) COMMENT "子用户ID",
    INDEX idx_device_id (deviceId),
		PRIMARY KEY(id)
);

# 设备在线状态表
CREATE TABLE IF NOT EXISTS device_online_state(
    deviceId varchar(32) COMMENT "设备ID",
    `online` TINYINT COMMENT "在线状态（0:离线，1:在线）",
    onlineTime TIMESTAMP COMMENT "上线时间",
    offlineTime TIMESTAMP COMMENT "离线时间",
		PRIMARY KEY(deviceId)
);

# 设备关联标签表
CREATE TABLE IF NOT EXISTS device_tag(
    id varchar(32),
    deviceId varchar(32) COMMENT "设备ID",
    `code` varchar(32) COMMENT "标签码",
    `name` varchar(32) COMMENT "标签名称",
    `value` varchar(100) COMMENT "标签值",
    INDEX idx_device_id (deviceId),
		PRIMARY KEY(id)
);

# 设备关联分组表
CREATE TABLE IF NOT EXISTS device_group_mapping(
    id varchar(32),
    deviceId varchar(32) COMMENT "设备ID",
    groupId varchar(32) COMMENT "分组码",
    INDEX idx_device_id (deviceId),
		PRIMARY KEY(id)
);








