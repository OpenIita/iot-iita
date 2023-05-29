package cc.iotkit.manager.dto.bo.protocolcomponent;


import cc.iotkit.common.api.PageRequest;
import cc.iotkit.model.protocol.ProtocolComponent;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: jay
 * @Date: 2023/5/29 10:43
 * @Version: V1.0
 * @Description: 组件查询
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProtocolComponent.class, reverseConvertGenerate = false)
public class ProtocolComponentBo extends PageRequest {

}
