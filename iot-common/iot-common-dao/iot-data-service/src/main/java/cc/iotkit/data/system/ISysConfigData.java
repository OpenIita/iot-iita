package cc.iotkit.data.system;

import cc.iotkit.common.api.Paging;
import cc.iotkit.data.manager.ICommonData;
import cc.iotkit.model.system.SysConfig;

/**
 * 系统配置数据接口
 *
 * @author sjg
 */
public interface ISysConfigData extends ICommonData<SysConfig, Long> {

    SysConfig selectPageConfigList(SysConfig query);
}
