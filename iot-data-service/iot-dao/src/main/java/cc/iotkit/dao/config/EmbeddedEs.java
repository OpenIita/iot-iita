package cc.iotkit.dao.config;

public interface EmbeddedEs {

    boolean disabled = "true".equals(System.getProperty("disabledEmbeddedEs"));

}
