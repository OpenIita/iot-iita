package cc.iotkit.data.manager;

import cc.iotkit.data.IOwnedData;
import cc.iotkit.model.protocol.ProtocolComponent;

import java.util.List;

public interface IProtocolComponentData extends IOwnedData<ProtocolComponent, String> {

    List<ProtocolComponent> findByState(String state);

    List<ProtocolComponent> findByStateAndType(String state, String type);

}
