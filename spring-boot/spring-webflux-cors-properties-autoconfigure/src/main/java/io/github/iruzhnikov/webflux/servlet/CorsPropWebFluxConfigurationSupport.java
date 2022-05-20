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

import io.github.iruzhnikov.webflux.config.CorsPropWebFluxConfiguration;
import io.github.iruzhnikov.webflux.config.SpringCorsProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebFluxAutoConfiguration.EnableWebFluxConfiguration.class)
@AutoConfigureAfter(CorsPropWebFluxConfiguration.class)
@ConditionalOnProperty(prefix = SpringCorsProperties.SPRING_FLUX_CORS, name = "enabled", havingValue = "true")
@ComponentScan(basePackageClasses = {CorsPropWebFluxConfigurationSupport.class})
public class CorsPropWebFluxConfigurationSupport extends DelegatingWebFluxConfiguration {

    private final SpringCorsProperties properties;

    public CorsPropWebFluxConfigurationSupport(SpringCorsProperties properties) {
        this.properties = properties;
    }

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CorsEndpointHandlerMapping(properties);
    }
}
