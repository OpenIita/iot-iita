package cc.iotkit.comp;

import cc.iotkit.comp.model.ReceiveResult;

import java.util.Map;

public interface IMessageHandler {

    ReceiveResult onReceive(Map<String, Object> head, String type, String msg);
}
