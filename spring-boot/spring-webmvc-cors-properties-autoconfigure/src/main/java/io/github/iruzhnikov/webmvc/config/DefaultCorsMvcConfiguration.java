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

package io.github.iruzhnikov.webmvc.config;

import io.github.iruzhnikov.webmvc.servlet.CorsPropWebMvcConfigurationSupport;
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
