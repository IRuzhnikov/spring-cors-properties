package com.github.iruzhnikov.web.servlet;

import com.github.iruzhnikov.web.config.SpringCorsProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/***
 * Cors auto add all methods to addAllowedMethod behavior equals
 * {@link org.springframework.web.bind.annotation.CrossOrigin} If you annotated type|method
 * {@link org.springframework.web.bind.annotation.CrossOrigin} then skip this logic for this type|method
 */
public class CorsEndpointHandlerMapping extends RequestMappingHandlerMapping {

    private static final PathMatcher DEFAULT_PATH_MATCHER = new AntPathMatcher();

    private final SpringCorsProperties properties;

    private PathMatcher pathMatcher = DEFAULT_PATH_MATCHER;

    public CorsEndpointHandlerMapping(SpringCorsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
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
                mappingInfo.getPatternsCondition() != null &&
                        mappingInfo.getPatternsCondition().getPatterns().stream().anyMatch(s -> {
                            String path = pathCorsConfiguration.getPath();
                            String[] allowedMethods = pathCorsConfiguration.getAllowedMethods();
                            return isEmpty(allowedMethods) && pathMatcher.match(path, s);
                        }));
    }
}
