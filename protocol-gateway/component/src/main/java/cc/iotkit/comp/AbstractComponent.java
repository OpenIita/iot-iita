package cc.iotkit.comp;

import cc.iotkit.converter.IConverter;
import lombok.Data;

@Data
public abstract class AbstractComponent implements IComponent {

    protected IMessageHandler handler;

    protected IConverter converter;

}
