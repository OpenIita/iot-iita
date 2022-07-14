package cc.iotkit.data;

import cc.iotkit.model.protocol.ProtocolComponent;

import java.util.List;

public interface IProtocolComponentData extends IOwnedData<ProtocolComponent, String> {

    List<ProtocolComponent> findByState(String state);

    List<ProtocolComponent> findByStateAndType(String state, String type);

}
