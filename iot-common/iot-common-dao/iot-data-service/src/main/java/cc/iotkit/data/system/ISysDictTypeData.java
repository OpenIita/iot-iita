package cc.iotkit.data.system;

import cc.iotkit.data.ICommonData;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.system.SysDictType;

/**
 * 字典类型数据接口
 *
 * @author sjg
 */
public interface ISysDictTypeData extends ICommonData<SysDictType, Long> {

    /**
     * 按条件分页查询
     */
    Paging<SysDictType> findByConditions(String dictName, String dictType,
                                         String status, int page, int size);

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    SysDictType findByDicType(String dictType);

}
