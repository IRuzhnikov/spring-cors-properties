package com.github.iruzhnikov.web.config;

import com.github.iruzhnikov.web.servlet.CorsPropWebMvcConfigurationSupport;
import com.github.iruzhnikov.web.servlet.CorsPropWebMvcRegistrations;
import com.github.iruzhnikov.web.servlet.SpringMvcCorsConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DelegatingWebMvcConfiguration.class)
@ConditionalOnProperty(prefix = SpringCorsProperties.SPRING_MVC_CORS, name = "enabled", havingValue = "true")
@ComponentScan(basePackageClasses = {CorsPropWebMvcConfigurationSupport.class})
@PropertySource(value = {"classpath:cors.yml"}, factory = YamlPropertyLoaderFactory.class)
public class CorsPropWebMvcConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = SpringCorsProperties.SPRING_MVC_CORS)
    public SpringCorsProperties springCorsProperties() {
        return new SpringCorsProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringMvcCorsConfigurer springMvcCorsConfigurer(SpringCorsProperties properties) {
        return new SpringMvcCorsConfigurer(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsPropWebMvcRegistrations corsPropWebMvcRegistrations(SpringCorsProperties properties) {
        return new CorsPropWebMvcRegistrations(properties);
    }
}
