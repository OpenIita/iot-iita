package cc.iotkit.temporal;

import cc.iotkit.model.Paging;
import cc.iotkit.model.device.message.ThingModelMessage;
import cc.iotkit.model.stats.TimeData;

import java.util.List;

public interface IThingModelMessageData {

    /**
     * 按消息类型和标识符取设备消息
     *
     * @param deviceId   设备id
     * @param type       消息类型
     * @param identifier 标识符
     * @param page       页码
     * @param size       页大小
     */
    Paging<ThingModelMessage> findByTypeAndIdentifier(String deviceId, String type,
                                                      String identifier, int page, int size);

    /**
     * 按用户统计时间段内上报次数
     *
     * @param uid   用户id
     * @param start 开始时间戳
     * @param end   结束时间戳
     */
    List<TimeData> getDeviceMessageStatsWithUid(String uid, long start, long end);

    void add(ThingModelMessage msg);

    long count();
}
