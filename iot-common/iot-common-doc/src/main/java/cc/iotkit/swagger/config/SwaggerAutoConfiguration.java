package cc.iotkit.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: 石恒
 * @Date: 2023/5/6 22:03
 * @Description:
 */
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@EnableKnife4j
public class SwaggerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {
        List<Docket> docketList = new LinkedList<>();
        if (swaggerProperties.getDocket().isEmpty()) {
            ApiSelectorBuilder docket = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo(swaggerProperties))
                    .enable(swaggerProperties.getEnabled())
                    .select()
                    .apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
            if (StringUtils.isNotBlank(swaggerProperties.getBasePackage())) {
                docket.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));
            }
            swaggerProperties.getBasePath().forEach(p -> docket.paths(PathSelectors.ant(p)));
            swaggerProperties.getExcludePath().forEach(p -> docket.paths(PathSelectors.ant(p).negate()));
            docketList.add(
                    docket.build()
                            .globalRequestParameters(getGlobalRequestParameters())
                            .globalResponses(HttpMethod.GET, getGlobalResponseMessage())
                            .globalResponses(HttpMethod.POST, getGlobalResponseMessage()));
        }
        swaggerProperties.getDocket().forEach((k, v) -> {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(k);
            ApiInfo apiInfo = new ApiInfoBuilder()
                    //页面标题
                    .title(docketInfo.getTitle())
                    //创建人
                    .contact(new Contact(docketInfo.getContact().getName(),
                            docketInfo.getContact().getUrl(),
                            docketInfo.getContact().getEmail()))
                    .version(docketInfo.getVersion())
                    .description(docketInfo.getDescription())
                    .build();
            ApiSelectorBuilder docket = new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo)
                    .enable(swaggerProperties.getEnabled())
                    .groupName(docketInfo.getGroup())
                    .select()
                    //为当前包路径
                    .apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
            if (StringUtils.isNotBlank(docketInfo.getBasePackage())) {
                docket.apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));
            }
            swaggerProperties.getBasePath().forEach(p -> docket.paths(PathSelectors.ant(p)));
            swaggerProperties.getExcludePath().forEach(p -> docket.paths(PathSelectors.ant(p).negate()));
            docketList.add(docket.build());
        });
        return docketList;
    }


    @Bean
    public ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .version(swaggerProperties.getVersion())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .build();
    }

    /**
     * 添加head参数配置
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("令牌")
                .required(false)
                .in(ParameterType.HEADER)
                .build());
        return parameters;
    }

    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }
}
