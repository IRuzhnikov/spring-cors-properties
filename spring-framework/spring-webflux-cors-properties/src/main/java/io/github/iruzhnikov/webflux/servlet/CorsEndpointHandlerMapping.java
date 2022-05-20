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

import io.github.iruzhnikov.webflux.config.SpringCorsProperties;
import org.springframework.http.server.PathContainer;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/***
 * Cors auto add all methods to addAllowedMethod behavior equals
 * {@link org.springframework.web.bind.annotation.CrossOrigin} If you annotated type|method
 * {@link org.springframework.web.bind.annotation.CrossOrigin} then skip this logic for this type|method
 */
public class CorsEndpointHandlerMapping extends RequestMappingHandlerMapping {

    private final SpringCorsProperties properties;

    public CorsEndpointHandlerMapping(SpringCorsProperties properties) {
        this.properties = properties;
    }

    @Override
    protected CorsConfiguration initCorsConfiguration(Object handler, Method method, RequestMappingInfo mappingInfo) {
        CorsConfiguration corsConfiguration = super.initCorsConfiguration(handler, method, mappingInfo);

        if (corsConfiguration == null && match(mappingInfo)) {
            CorsConfiguration config = new CorsConfiguration();
            for (RequestMethod allowedMethod : mappingInfo.getMethodsCondition().getMethods()) {
                config.addAllowedMethod(allowedMethod.name());
            }
            return config.applyPermitDefaultValues();
        }

        return corsConfiguration;
    }

    private boolean match(RequestMappingInfo mappingInfo) {
        return properties.getMappings().values().stream().anyMatch(pathCorsConfiguration ->
                mappingInfo.getPatternsCondition().getPatterns().stream().anyMatch(s -> {
                    String path = pathCorsConfiguration.getPath();
                    String[] allowedMethods = pathCorsConfiguration.getAllowedMethods();
                    return isEmpty(allowedMethods) && s.matches(PathContainer.parsePath(path));
                }));
    }
}
