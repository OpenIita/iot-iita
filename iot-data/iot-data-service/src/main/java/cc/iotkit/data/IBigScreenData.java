package cc.iotkit.data;

import cc.iotkit.model.screen.BigScreen;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:56
 */
public interface IBigScreenData extends IOwnedData<BigScreen, String>  {

    BigScreen findByUidAndIsDefault(String uid, boolean isDefault);

    List<BigScreen>  findByState(String state);
}
