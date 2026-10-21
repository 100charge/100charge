package com.xingchuan.web.core.config;

import java.util.ArrayList;

import com.xingchuan.common.config.BaseConfig;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Swagger3（springdoc-openapi）的接口配置
 * 原 springfox 实现已迁移至 springdoc-openapi 2.0.4（JDK17 + Spring Boot 2.6.15 兼容线）。
 *
 * @author xingchuan
 */
@Configuration
public class SwaggerConfig {
    /**
     * 系统基础配置
     */
    @Resource
    private BaseConfig baseConfig;

    /**
     * 设置请求的统一前缀
     */
    @Value("${swagger.pathMapping}")
    private String pathMapping;

    /**
     * 创建API
     */
    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI()
                .info(new Info()
                        // 设置标题
                        .title("标题:后台管理系统_接口文档")
                        // 描述
                        .description("描述:接口文档")
                        // 作者信息
                        .contact(new Contact().name(baseConfig.getName()))
                        // 版本
                        .version("版本号:" + baseConfig.getVersion()))
                /* 设置安全模式，swagger可以设置访问token */
                .components(new Components().addSecuritySchemes("Authorization",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    /**
     * 设置请求的统一前缀（等价于 springfox 的 pathMapping，供 Swagger UI "Try it out" 使用）
     */
    @Bean
    public OpenApiCustomizer pathMappingCustomizer() {
        return openApi -> {
            if (openApi.getServers() == null) {
                openApi.setServers(new ArrayList<>());
            }
            openApi.getServers().add(new Server().url(pathMapping));
        };
    }
}
