package cc.iotkit.swagger.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Author: 石恒
 * @Date: 2023/5/4 20:12
 * @Description:
 */
@Component
@EnableSwagger2WebMvc

public class SwaggerConfig {

    @Value("${spring.application.name:Swagger API}")
    private String applicationName;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(applicationName)
                .enable(true)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description("Swagger API Doc")
                .build();
    }

    // 解决springboot升级到2.6.x之后，knife4j报错
    @Bean
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                if (bean instanceof WebMvcRequestHandlerProvider ) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                mappings.removeIf(mapping -> mapping.getPatternParser() != null);
            }

            @SuppressWarnings("unchecked")
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                try {
                    Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                    field.setAccessible(true);
                    return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        };
    }

//    /**
//     * 解决springboot升级到2.6.x之后，knife4j报错
//     *
//     * @param webEndpointsSupplier        the web endpoints supplier
//     * @param servletEndpointsSupplier    the servlet endpoints supplier
//     * @param controllerEndpointsSupplier the controller endpoints supplier
//     * @param endpointMediaTypes          the endpoint media types
//     * @param corsProperties              the cors properties
//     * @param webEndpointProperties       the web endpoint properties
//     * @param environment                 the environment
//     * @return the web mvc endpoint handler mapping
//     */
//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
//            WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
//            ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
//            CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties,
//            Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = shouldRegisterLinksMapping(webEndpointProperties,
//                environment, basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
//                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
//                shouldRegisterLinksMapping, null);
//    }
//
//    /**
//     * shouldRegisterLinksMapping
//     * @param webEndpointProperties webEndpointProperties
//     * @param environment environment
//     * @param basePath /
//     * @return boolean
//     */
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties,
//                                               Environment environment, String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
//                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//    }
}
