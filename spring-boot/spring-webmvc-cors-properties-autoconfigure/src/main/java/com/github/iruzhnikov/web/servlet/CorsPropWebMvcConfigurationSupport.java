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

package com.github.iruzhnikov.web.servlet;

import com.github.iruzhnikov.web.config.CorsPropWebMvcConfiguration;
import com.github.iruzhnikov.web.config.SpringCorsProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class)
@AutoConfigureAfter(CorsPropWebMvcConfiguration.class)
@ConditionalOnProperty(prefix = SpringCorsProperties.SPRING_MVC_CORS, name = "enabled", havingValue = "true")
@ComponentScan(basePackageClasses = {CorsPropWebMvcConfigurationSupport.class})
public class CorsPropWebMvcConfigurationSupport extends DelegatingWebMvcConfiguration {

    private final SpringCorsProperties properties;

    public CorsPropWebMvcConfigurationSupport(SpringCorsProperties properties) {
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
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CorsEndpointHandlerMapping(properties);
    }
}
