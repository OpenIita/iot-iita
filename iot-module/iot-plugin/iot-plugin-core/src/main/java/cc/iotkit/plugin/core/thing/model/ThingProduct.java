package cc.iotkit.plugin.core.thing.model;

import lombok.Data;

/**
 * 产品信息
 *
 * @author sjg
 */
@Data
public class ThingProduct {

    private String productKey;

    private String productSecret;

    private String name;

    private String category;

    private Integer nodeType;

}
