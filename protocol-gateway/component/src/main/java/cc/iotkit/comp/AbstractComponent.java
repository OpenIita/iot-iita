package cc.iotkit.comp;

import cc.iotkit.converter.IConverter;
import lombok.Data;

@Data
public abstract class AbstractComponent implements IComponent {

    protected IMessageHandler handler;

    protected IConverter converter;

    protected CompConfig config;

    @Override
    public void create(CompConfig config) {
        this.config=config;
    }

    @Override
    public CompConfig getConfig() {
        return config;
    }
}
