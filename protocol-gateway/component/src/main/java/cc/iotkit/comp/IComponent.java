package cc.iotkit.comp;

import cc.iotkit.converter.IConverter;

public interface IComponent {

    void create(String config);

    void start();

    void stop();

    void destroy();

    void setHandler(IMessageHandler handler);

    void setConverter(IConverter converter);

    IConverter getConverter();

}
