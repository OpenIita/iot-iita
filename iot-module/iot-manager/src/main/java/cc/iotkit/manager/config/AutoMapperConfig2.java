package cc.iotkit.manager.config;

import io.github.linpeilie.annotations.ComponentModelConfig;
import io.github.linpeilie.annotations.MapperConfig;

/**
 * @Author: jay
 * @Date: 2023/6/4 14:21
 * @Version: V1.0
 * @Description: mapperstruct 配置
 */
@MapperConfig(mapperPackage = "cc.iotkit.mapstruct",
        adapterPackage="cc.iotkit.manager.adapter", adapterClassName = "managerAdapter")
public class AutoMapperConfig2 {
}
