//package cc.iotkit.common.tenant.handle;
//
//
//import cc.iotkit.common.constant.GlobalConstants;
//import cc.iotkit.common.redis.handler.KeyPrefixHandler;
//import cc.iotkit.common.tenant.helper.TenantHelper;
//import cc.iotkit.common.utils.StringUtils;
//
///**
// * 多租户redis缓存key前缀处理
// *
// * @author Lion Li
// */
//public class TenantKeyPrefixHandler extends KeyPrefixHandler {
//
//    public TenantKeyPrefixHandler(String keyPrefix) {
//        super(keyPrefix);
//    }
//
//    /**
//     * 增加前缀
//     */
//    @Override
//    public String map(String name) {
//        if (StringUtils.isBlank(name)) {
//            return null;
//        }
//        if (StringUtils.contains(name, GlobalConstants.GLOBAL_REDIS_KEY)) {
//            return super.map(name);
//        }
//        String tenantId = TenantHelper.getTenantId();
//        if (StringUtils.startsWith(name, tenantId)) {
//            // 如果存在则直接返回
//            return super.map(name);
//        }
//        return super.map(tenantId + ":" + name);
//    }
//
//    /**
//     * 去除前缀
//     */
//    @Override
//    public String unmap(String name) {
//        String unmap = super.unmap(name);
//        if (StringUtils.isBlank(unmap)) {
//            return null;
//        }
//        if (StringUtils.contains(name, GlobalConstants.GLOBAL_REDIS_KEY)) {
//            return super.unmap(name);
//        }
//        String tenantId = TenantHelper.getTenantId();
//        if (StringUtils.startsWith(unmap, tenantId)) {
//            // 如果存在则删除
//            return unmap.substring((tenantId + ":").length());
//        }
//        return unmap;
//    }
//
//}
