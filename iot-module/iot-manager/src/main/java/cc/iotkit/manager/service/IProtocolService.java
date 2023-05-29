package cc.iotkit.manager.service;

import cc.iotkit.model.protocol.ProtocolComponent;

/**
 * @Author: jay
 * @Date: 2023/5/29 11:28
 * @Version: V1.0
 * @Description: 协议组件接口
 */
public interface IProtocolService {

    // 上传jar包
    String uploadJar(String jarFile, String id);

    // 添加组件
    boolean addComponent(ProtocolComponent component);


}
