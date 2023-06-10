package cc.iotkit.data.manager;

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.ota.OtaPackage;

import java.util.List;

/**
 * @Author: 石恒
 * @Date: 2023/5/19 21:46
 * @Description:
 */
public interface IOtaPackageData extends ICommonData<OtaPackage, String> {

    List<OtaPackage> findByVersionGreaterThan(String version);

}
