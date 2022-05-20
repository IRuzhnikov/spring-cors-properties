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

package io.github.iruzhnikov.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.autoconfigure.web.reactive.WebTestClientAutoConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.reactive.server.AbstractReactiveWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Fix initialization test client, because {@link WebTestClientAutoConfiguration#webTestClient(ApplicationContext, List, List)}
 * not contains baseUrl initialization like of {@link org.springframework.boot.test.web.reactive.server.WebTestClientContextCustomizer.WebTestClientFactory#getObject()}
 */
public class BaseUrlWebTestClientBuilderCustomizer extends DefaultUriBuilderFactory implements
        WebTestClientBuilderCustomizer, Ordered,
        ApplicationListener<WebServerInitializedEvent> {
    private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";
    private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

    private final ApplicationContext context;
    private final UriComponentsBuilder baseUri;

    public BaseUrlWebTestClientBuilderCustomizer(ApplicationContext context, UriComponentsBuilder baseUri) {
        super(baseUri);
        this.context = context;
        this.baseUri = baseUri;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent ignored) {
        boolean sslEnabled = isSslEnabled(context);
        String port = context.getEnvironment().getProperty("local.server.port", "8080");
        String baseUrl = getBaseUrl(sslEnabled, port);
        baseUri.uri(URI.create(baseUrl));
    }

    @Override
    public void customize(WebTestClient.Builder builder) {
        builder.uriBuilderFactory(this);
    }

    private boolean isSslEnabled(ApplicationContext context) {
        try {
            AbstractReactiveWebServerFactory webServerFactory = context
                    .getBean(AbstractReactiveWebServerFactory.class);
            return webServerFactory.getSsl() != null && webServerFactory.getSsl().isEnabled();
        } catch (NoSuchBeanDefinitionException ex) {
            return false;
        }
    }

    private String getBaseUrl(boolean sslEnabled, String port) {
        String basePath = deduceBasePath();
        String pathSegment = (StringUtils.hasText(basePath)) ? basePath : "";
        return (sslEnabled ? "https" : "http") + "://localhost:" + port + pathSegment;
    }

    private String deduceBasePath() {
        WebApplicationType webApplicationType = deduceFromApplicationContext(context.getClass());
        if (webApplicationType == WebApplicationType.REACTIVE) {
            return context.getEnvironment().getProperty("spring.webflux.base-path");
        } else if (webApplicationType == WebApplicationType.SERVLET) {
            return requireNonNull(((WebApplicationContext) context).getServletContext()).getContextPath();
        }
        return null;
    }

    private WebApplicationType deduceFromApplicationContext(Class<?> applicationContextClass) {
        if (isAssignable(SERVLET_APPLICATION_CONTEXT_CLASS, applicationContextClass)) {
            return WebApplicationType.SERVLET;
        }
        if (isAssignable(REACTIVE_APPLICATION_CONTEXT_CLASS, applicationContextClass)) {
            return WebApplicationType.REACTIVE;
        }
        return WebApplicationType.NONE;
    }

    private static boolean isAssignable(String target, Class<?> type) {
        try {
            return ClassUtils.resolveClassName(target, null).isAssignableFrom(type);
        } catch (Throwable ex) {
            return false;
        }
    }
}
