package cc.iotkit.temporal.td.config;

public interface Constants {

    /**
     * 根据产品key获取产品属性超级表名
     */
    static String getProductPropertySTableName(String productKey) {
        return String.format("product_property_%s", productKey.toLowerCase());
    }

    /**
     * 根据deviceId获取设备属性表名
     */
    static String getDevicePropertyTableName(String deviceId) {
        return String.format("device_property_%s", deviceId.toLowerCase());
    }
}
