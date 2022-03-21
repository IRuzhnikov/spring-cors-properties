package com.github.iruzhnikov.web.servlet;

import com.github.iruzhnikov.web.config.SpringCorsProperties;
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
