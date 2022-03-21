package com.github.iruzhnikov.web.config;

import com.github.iruzhnikov.web.servlet.CorsPropWebMvcConfigurationSupport;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(CorsPropWebMvcConfigurationSupport.class)
@ConditionalOnProperty(prefix = SpringCorsProperties.SPRING_MVC_CORS, name = "enabled", havingValue = "false")
public class DefaultCorsMvcConfiguration {
}
