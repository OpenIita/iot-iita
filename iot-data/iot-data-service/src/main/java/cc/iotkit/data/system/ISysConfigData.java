package cc.iotkit.data.system;

import cc.iotkit.data.manager.ICommonData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.system.SysConfig;

/**
 * 系统配置数据接口
 *
 * @author sjg
 */
public interface ISysConfigData extends ICommonData<SysConfig, Long> {

    /**
     * 按条件分页查询
     */
    Paging<SysConfig> findByConditions(String configKey, String configName,
                                       int page, int size);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    SysConfig findByConfigKey(String configKey);

}
