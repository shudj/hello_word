package com.example.restful.util;

import com.google.common.collect.Sets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: shudj
 * @time: 2019/2/15 17:03
 * @description:
 */

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(Sets.newHashSet("http"))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.restful"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("Rest接口定义")
                .version("1.0")
                .description("用户测试RESTful API")
                .build();
    }
}
