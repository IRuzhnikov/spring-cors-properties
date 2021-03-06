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

package io.github.iruzhnikov.webmvc.servlet;

import io.github.iruzhnikov.webmvc.config.SpringCorsProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class CorsPropWebMvcRegistrations implements WebMvcRegistrations {
    private final SpringCorsProperties properties;

    public CorsPropWebMvcRegistrations(SpringCorsProperties properties) {
        this.properties = properties;
    }

    /**
     * Handled before
     * {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#createRequestMappingHandlerMapping}
     * handler order use
     * {@link org.springframework.web.servlet.DispatcherServlet#initHandlerExceptionResolvers}
     * handler resolver
     * {@link org.springframework.web.servlet.DispatcherServlet#getHandler}
     */
    @Override
    @SuppressWarnings("JavadocReference")
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new CorsEndpointHandlerMapping(properties);
    }
}
