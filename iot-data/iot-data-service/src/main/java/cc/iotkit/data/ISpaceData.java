package cc.iotkit.data;

import cc.iotkit.model.space.Space;

import java.util.List;

public interface ISpaceData extends IOwnedData<Space,String> {

    List<Space> findByUidOrderByCreateAtDesc(String uid);

    List<Space> findByUidAndHomeIdOrderByCreateAtDesc(String uid, String homeId);

    List<Space> findByHomeId(String homeId);

}
