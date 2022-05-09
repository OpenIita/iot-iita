package cc.iotkit.common;

public interface Constants {

    String PRODUCT_SECRET = "xdkKUymrEGSCYWswqCvSPyRSFvH5j7CU";

    String ACCOUNT_SECRET = "3n1z33kzvpgz1foijpkepyd3e8tw84us";

    String PRODUCT_CACHE = "product_cache";

    String DEVICE_CACHE = "device_cache";

    String CATEGORY_CACHE = "category_cache";

    String SPACE_CACHE = "space_cache";

    String THING_MODEL_CACHE = "thing_model_cache";

    String WECHAT_APP_ID = "wx791cb7bf75950e0c";

    String WECHAT_APP_SECRET = "eeef73ce71f1a722ad6298985d859844";

    String APP_DESIGN_CACHE = "app_design_cache";

    String PRODUCT_SCRIPT_CACHE = "product_script_cache";

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
    String ROLE_SYSTEM = "iot_system_user";

    /**
     * C端用户角色
     */
    String ROLE_CLIENT = "iot_client_user";

    /**
     * C端用户默认密码
     */
    String PWD_CLIENT_USER = "c123456";

    /**
     * 系统用户默认密码
     */
    String PWD_SYSTEM_USER = "s123456";

    /**
     * 设备原始上报消息的topic
     */
    String DEVICE_RAW_MESSAGE_TOPIC = "device_raw";

    /**
     * 设备物模型消息的topic
     */
    String THING_MODEL_MESSAGE_TOPIC = "device_thing";


    interface API {

        /**
         * 设备-基路径
         */
        String DEVICE_BASE = "/device";

        /**
         * 设备-设备列表
         */
        String DEVICE_LIST = "/list/{size}/{page}";

        /**
         * 设备-设备详情
         */
        String DEVICE_DETAIL = "/{deviceId}/detail";

        /**
         * 设备-属性设置
         */
        String DEVICE_SET_PROPERTIES = "/{deviceId}/service/property/set";

        /**
         * 设备-服务调用
         */
        String DEVICE_INVOKE_SERVICE = "/{deviceId}/service/{service}/invoke";

    }

}
