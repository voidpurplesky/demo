package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${com.example.upload.path}") // import 시에 springframework로 시작하는 Value
    private String uploadPath;
/*
    private final String baseUrl;
    public WebMvcConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }
*/
// resource handling - js, css, image와 같은 정적 리소스를 어떻게 제어할지 구성
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        /*
        o.s.web.servlet.PageNotFound             : No mapping for GET /js/scripts.js

         */
        registry.addResourceHandler("/asset/**").addResourceLocations("classpath:/static/asset/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        //String baseUrl = StringUtils.trimTrailingCharacter(this.baseUrl, '/');
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")

                .resourceChain(false);

        registry.addResourceHandler("/view/**")
                .addResourceLocations("file:///D:/upload/");
        /*
        registry.addResourceHandler( "/webjars/**")
               .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");

         */

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/")
                .setViewName("forward:/swagger-ui/index.html");
    }
}
