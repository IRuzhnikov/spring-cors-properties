package com.github.iruzhnikov.web.servlet;

import com.github.iruzhnikov.web.config.PathCorsConfiguration;
import com.github.iruzhnikov.web.config.SpringCorsProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

/**
 * Load and patch CORS configuration from properties file to CORS mappings
 */
@CommonsLog
public class SpringMvcCorsConfigurer implements WebMvcConfigurer {

    private final SpringCorsProperties properties;

    public SpringMvcCorsConfigurer(SpringCorsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (properties.getMappings().isEmpty()) {
            registry.addMapping(PathCorsConfiguration.ALL_PATH);
            return;
        } else {
            String errors = properties.getMappings().entrySet().stream()
                    .filter(entry -> StringUtils.isBlank(entry.getValue().getPath()))
                    .map(entry -> String.format("Not support empty path for property key: %s.mappings.%s",
                            SpringCorsProperties.SPRING_MVC_CORS, entry.getKey()))
                    .collect(Collectors.joining(";" + System.lineSeparator()));
            if (StringUtils.isNotBlank(errors)) {
                throw new IllegalArgumentException(errors);
            }
        }
        properties.getMappings().forEach((s, config) -> configure(registry, config));
    }

    private void configure(CorsRegistry registry, PathCorsConfiguration config) {
        if (log.isInfoEnabled()) {
            log.info("Cors configure: " + config.getPath());
        }
        if (log.isDebugEnabled()) {
            log.debug("Cors custom details: " + config);
        }
        CorsRegistration mapping = registry.addMapping(config.getPath());
        if (isNotEmpty(config.getAllowedOrigins())) {
            mapping.allowedOrigins(config.getAllowedOrigins());
        }
        if (isNotEmpty(config.getAllowedOriginPatterns())) {
            mapping.allowedOriginPatterns(config.getAllowedOriginPatterns());
        }
        if (isNotEmpty(config.getAllowedHeaders())) {
            mapping.allowedHeaders(config.getAllowedHeaders());
        }
        if (isNotEmpty(config.getAllowedMethods())) {
            mapping.allowedMethods(config.getAllowedMethods());
        }
        if (isNotEmpty(config.getExposedHeaders())) {
            mapping.exposedHeaders(config.getExposedHeaders());
        }
        if (config.getMaxAge() != null) {
            mapping.maxAge(config.getMaxAge().getSeconds());
        }
        if (config.getAllowCredentials() != null) {
            mapping.allowCredentials(config.getAllowCredentials());
        }
    }

    /**
     * Override for suppress idea inspection
     */
    @Contract("null -> false")
    private <T> boolean isNotEmpty(@Nullable T[] inspect) {
        return ArrayUtils.isNotEmpty(inspect);
    }
}


