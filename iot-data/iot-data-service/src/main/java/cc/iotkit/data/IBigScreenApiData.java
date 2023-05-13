package cc.iotkit.data;

import cc.iotkit.model.screen.BigScreen;
import cc.iotkit.model.screen.BigScreenApi;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:56
 */
public interface IBigScreenApiData extends IOwnedData<BigScreenApi, String>  {

    List<BigScreenApi> findByScreenId(String id);

    void saveApiList(BigScreen screen, List<BigScreenApi> screenApis);
}
