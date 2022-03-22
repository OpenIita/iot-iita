package cc.iotkit.comp;

import cc.iotkit.converter.Converter;
import lombok.Data;

@Data
public abstract class AbstractComponent implements Component {

    protected MessageHandler handler;

    protected Converter converter;

}
