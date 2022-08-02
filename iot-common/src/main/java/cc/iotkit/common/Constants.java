/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.common;

public interface Constants {

    String PRODUCT_SECRET = "xdkKUymrEGSCYWswqCvSPyRSFvH5j7CU";

    String ACCOUNT_SECRET = "3n1z33kzvpgz1foijpkepyd3e8tw84us";

    String CACHE_PRODUCT = "product_cache";

    String CACHE_DEVICE_INFO = "device_info_cache";

    String CACHE_DEVICE_STATS = "device_stats_cache";

    String CACHE_CATEGORY = "category_cache";

    String CACHE_SPACE = "space_cache";

    String THING_MODEL_CACHE = "thing_model_cache";

    String CACHE_USER_INFO = "user_info_cache";

    String CACHE_OAUTH_CLIENT = "oauth_client_cache";

    String WECHAT_APP_ID = "wx791cb7bf75950e0c";

    String WECHAT_APP_SECRET = "eeef73ce71f1a722ad6298985d859844";

    String APP_DESIGN_CACHE = "app_design_cache";

    String CACHE_PRODUCT_SCRIPT = "product_script_cache";

    /**
     * 管理员角色
     */
    String ROLE_ADMIN = "iot_admin";

    /**
     * 可写角色
     */
    String ROLE_WRITE = "iot_write";

    /**
     * 管理系统用户角色
     */
    String ROLE_SYSTEM = "iot_system";

    /**
     * C端用户角色
     */
    String ROLE_CLIENT = "iot_client";

    /**
     * C端用户默认密码
     */
    String PWD_CLIENT_USER = "c123456";

    /**
     * 系统用户默认密码
     */
    String PWD_SYSTEM_USER = "s123456";

    /**
     * 设备物模型消息的topic
     */
    String THING_MODEL_MESSAGE_TOPIC = "device_thing";

    /**
     * 设备属性上报消息的topic
     */
    String DEVICE_PROPERTY_REPORT_TOPIC = "device_property_report";

    /**
     * 不含消息内容的设备上报记录的topic，用于统计
     */
    String DEVICE_REPORT_RECORD_TOPIC = "device_report_record";

    /**
     * 设备配置消息topic
     */
    String DEVICE_CONFIG_TOPIC = "device_config";

    /**
     * http消费设备信息的topic
     */
    String HTTP_CONSUMER_DEVICE_INFO_TOPIC = "device_info:";

    /**
     * 写权限
     */
    String PERMISSION_WRITE = "write";

    /**
     * 三方平台类型
     */
    enum ThirdPlatform {
        dueros("小度"),
        aligenie("天猫精灵"),
        miiot("小爱");

        public String desc;

        ThirdPlatform(String desc) {
            this.desc = desc;
        }
    }

    /**
     * 三方平台openUid名称
     */
    enum ThirdOpenUid {
        duerosOpenUid("小度OpenUid"),
        aligenieOpenUid("天猫精灵OpenUid"),
        miiotOpenUid("小爱OpenUid");

        public String desc;

        ThirdOpenUid(String desc) {
            this.desc = desc;
        }
    }

    interface API_DEVICE {

        /**
         * 设备-基路径
         */
        String BASE = "/device";

        /**
         * 设备-设备列表
         */
        String LIST = "/list/{size}/{page}";

        /**
         * 设备-设备详情
         */
        String DETAIL = "/{deviceId}/detail";

        /**
         * 设备-属性设置
         */
        String SET_PROPERTIES = "/{deviceId}/service/property/set";

        /**
         * 设备-服务调用
         */
        String INVOKE_SERVICE = "/{deviceId}/service/{service}/invoke";

    }

    interface API_SPACE {

        /**
         * 空间-基路径
         */
        String BASE = "/space";

        /**
         * 最近使用设备列表
         */
        String RECENT_DEVICES = "/myRecentDevices";

        /**
         * 我的空间设备列表
         */
        String SPACE_DEVICES = "/myDevices/{spaceId}";

        /**
         * 查找设备
         */
        String FIND_DEVICE = "/findDevice";

        /**
         * 空间添加设备
         */
        String ADD_DEVICE = "/addDevice";

        /**
         * 空间删除设备
         */
        String REMOVE_DEVICE = "/removeDevice";

        /**
         * 空间修改设备
         */
        String SAVE_DEVICE = "/saveDevice";

        /**
         * 获取空间设备信息
         */
        String GET_DEVICE = "/device/{deviceId}";

        /**
         * 设置第三方平台openUid
         */
        String SET_OPEN_UID = "/setOpenUid";
    }

    interface MQTT {
        String DEVICE_SUBSCRIBE_TOPIC = "^/sys/.+/.+/c/#$";
    }
}
