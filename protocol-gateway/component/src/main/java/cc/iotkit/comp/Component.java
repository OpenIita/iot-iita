package cc.iotkit.comp;

import cc.iotkit.converter.Converter;

public interface Component {

    void create(String config);

    void start();

    void stop();

    void destroy();

    void setHandler(MessageHandler handler);

    void setConverter(Converter converter);

    Converter getConverter();

}
