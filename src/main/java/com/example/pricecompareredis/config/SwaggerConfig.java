package com.example.pricecompareredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /*
    Swagger 설정을 위한 클래스
    api 문서화를 위한 설정을 구현한다.
    Docket 객체를 생성하여 REST 컨트롤러의 위치를 지정하고, API 정보를 구성한다.
    이를 통해 LowPriceWithRedis API 문서를 자동으로 생성하고, 서비스의 모든 API를 한눈에 볼 수 있게 한다.
    */
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.example.pricecompareredis.controller"))
                .build().apiInfo(apiInfo());
    }
    // .apis에 controller 패키지를 지정해주면 해당 패키지에 있는 controller들만 문서화된다

    private ApiInfo apiInfo() {
        String documentDesc = "LowPriceWithRedis API Document";
        return new ApiInfoBuilder()
                .title("LowPriceWithRedis API")
                .description(documentDesc)
                .version("1.0")
                .build();
    }
}
