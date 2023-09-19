package cc.iotkit.plugin.core.thing.actions;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author sjg
 */
@Data
@SuperBuilder
public abstract class AbstractAction implements IDeviceAction {

    protected String id;

    protected ActionType type;

    protected String productKey;

    protected String deviceName;

    protected Long time;

    public AbstractAction() {
    }

    public AbstractAction(ActionType type) {
        this.type = type;
    }
}
