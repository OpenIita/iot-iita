package cc.iotkit.plugin.core.thing.actions.up;


import cc.iotkit.plugin.core.thing.actions.AbstractAction;
import cc.iotkit.plugin.core.thing.actions.ActionType;
import cc.iotkit.plugin.core.thing.actions.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 设备在线状态变更
 *
 * @author sjg
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class DeviceStateChange extends AbstractAction {

    private DeviceState state;

    @Override
    public ActionType getType() {
        return ActionType.STATE_CHANGE;
    }
}
