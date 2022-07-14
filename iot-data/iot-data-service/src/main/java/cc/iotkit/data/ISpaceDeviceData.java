package cc.iotkit.data;

import cc.iotkit.model.space.SpaceDevice;

import java.util.List;

public interface ISpaceDeviceData extends IOwnedData<SpaceDevice, String> {

    List<SpaceDevice> findByUidOrderByUseAtDesc(String uid);

    List<SpaceDevice> findByUidOrderByAddAtDesc(String uid);

    List<SpaceDevice> findBySpaceIdOrderByAddAtDesc(String spaceId);

    List<SpaceDevice> findByUidAndSpaceIdOrderByAddAtDesc(String uid, String spaceId);

    SpaceDevice findByDeviceId(String deviceId);

    SpaceDevice findByDeviceIdAndUid(String deviceId, String uid);
}
