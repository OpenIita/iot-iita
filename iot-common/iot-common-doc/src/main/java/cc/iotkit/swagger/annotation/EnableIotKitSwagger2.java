package cc.iotkit.swagger.annotation;

import cc.iotkit.swagger.config.SwaggerProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: 石恒
 * @Date: 2023/5/6 22:12
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SwaggerProperties.class)
public @interface EnableIotKitSwagger2 {

}
