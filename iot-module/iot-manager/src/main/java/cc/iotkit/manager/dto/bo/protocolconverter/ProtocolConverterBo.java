package cc.iotkit.manager.dto.bo.protocolconverter;


import cc.iotkit.common.api.PageRequest;
import cc.iotkit.model.protocol.ProtocolConverter;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: jay
 * @Date: 2023/5/29 10:48
 * @Version: V1.0
 * @Description: 转换脚本查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = ProtocolConverter.class, reverseConvertGenerate = false)
public class ProtocolConverterBo extends PageRequest {
}
