package com.object.haru.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *    Swagger의 설정을 관리하는 Config 파일
 *
 *   @version          1.00    2023.02.07
 *   @author           한승완
 */
    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {

        @Bean
    public Docket api() {
            ParameterBuilder aParameterBuilder = new ParameterBuilder();
            aParameterBuilder.name("X-Auth-Token") //헤더 이름
                    .description("Access Tocken") //설명
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build();

            List<Parameter> aParameters = new ArrayList<>();
            aParameters.add(aParameterBuilder.build());


            return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(aParameters).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .enable(true);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API 타이틀")
                .description("API 상세소개 및 사용법")
                .version("1.0")
                .build();
    }

}