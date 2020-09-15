package com.boug.employee.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableOpenApi
@EnableSwagger2
public class SwaggerConfiguration implements WebMvcConfigurer {
    @Value("${base.swagger.url}")
    private String baseUrl;

    public SwaggerConfiguration() {
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');

        registry.addResourceHandler(baseUrl + "/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(baseUrl + "/swagger-ui/")
                .setViewName("forward:" + baseUrl + "/swagger-ui/index.html");
    }

    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfo(
                "API",
                "Employee Rest API Documentation",
                "2.0",
                null,
                null,
                null,
                null,
                Collections.EMPTY_LIST
        );


        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.boug.employee"))
                .build()
                .host("")
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(false)
                .produces(Collections.singleton(MediaType.APPLICATION_JSON_UTF8_VALUE))
                ;
    }

}