package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.system.SysRoleDept;

/**
 * 操作日志数据接口
 *
 * @author sjg
 */
public interface ISysRoleDeptData extends ICommonData<SysRoleDept, Long> {

    void delete(Long roleId);

}
