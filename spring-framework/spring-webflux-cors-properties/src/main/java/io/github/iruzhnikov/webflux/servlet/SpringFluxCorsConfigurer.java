/*
 * Copyright (c) 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.iruzhnikov.webflux.servlet;

import io.github.iruzhnikov.webflux.config.PathCorsConfiguration;
import io.github.iruzhnikov.webflux.config.SpringCorsProperties;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.config.CorsRegistration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import javax.annotation.Nullable;
import java.util.stream.Collectors;

/**
 * Load and patch CORS configuration from properties file to CORS mappings
 */
@CommonsLog
public class SpringFluxCorsConfigurer implements WebFluxConfigurer {

    private final SpringCorsProperties properties;

    public SpringFluxCorsConfigurer(SpringCorsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (properties.getMappings().isEmpty()) {
            registry.addMapping(PathCorsConfiguration.ALL_PATH);
            return;
        } else {
            String errors = properties.getMappings().entrySet().stream()
                    .filter(entry -> CollectionUtils.isEmpty(entry.getValue().getPaths()))
                    .map(entry -> String.format("Not support empty path for property key: %s.mappings.%s",
                            SpringCorsProperties.SPRING_FLUX_CORS, entry.getKey()))
                    .collect(Collectors.joining(";" + System.lineSeparator()));
            if (StringUtils.isNotBlank(errors)) {
                throw new IllegalArgumentException(errors);
            }
        }
        properties.getMappings().forEach((s, config) -> configure(registry, config));
    }

    protected void configure(CorsRegistry registry, PathCorsConfiguration config) {
        if (log.isInfoEnabled()) {
            log.info("Cors configure: " + config.getPaths());
        }
        if (log.isDebugEnabled()) {
            log.debug("Cors custom details: " + config);
        }
        config.getPaths().forEach(path -> configurePath(registry, config, path));
    }

    protected void configurePath(CorsRegistry registry, PathCorsConfiguration config, String path) {
        CorsRegistration mapping = registry.addMapping(path);
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
    protected static <T> boolean isNotEmpty(@Nullable T[] inspect) {
        return ArrayUtils.isNotEmpty(inspect);
    }
}


