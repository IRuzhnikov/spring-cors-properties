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

package io.github.iruzhnikov.webflux.config;

import io.github.iruzhnikov.webflux.servlet.CorsPropWebFluxConfigurationSupport;
import io.github.iruzhnikov.webflux.servlet.CorsPropWebFluxRegistrations;
import io.github.iruzhnikov.webflux.servlet.SpringFluxCorsConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(DelegatingWebFluxConfiguration.class)
@ConditionalOnProperty(prefix = SpringCorsProperties.SPRING_FLUX_CORS, name = "enabled", havingValue = "true")
@ComponentScan(basePackageClasses = {CorsPropWebFluxConfigurationSupport.class})
public class CorsPropWebFluxConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = SpringCorsProperties.SPRING_FLUX_CORS)
    public SpringCorsProperties springCorsProperties() {
        return new SpringCorsProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringFluxCorsConfigurer springMvcCorsConfigurer(SpringCorsProperties properties) {
        return new SpringFluxCorsConfigurer(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsPropWebFluxRegistrations corsPropWebFluxRegistrations(SpringCorsProperties properties) {
        return new CorsPropWebFluxRegistrations(properties);
    }
}
