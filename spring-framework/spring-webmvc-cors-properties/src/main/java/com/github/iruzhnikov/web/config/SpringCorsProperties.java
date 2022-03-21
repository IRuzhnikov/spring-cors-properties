package com.github.iruzhnikov.web.config;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SpringCorsProperties {
    //Base property path
    public static final String SPRING_MVC_CORS = "spring.mvc.cors";
    private final Map<String, PathCorsConfiguration> mappings = new HashMap<>();
    private Boolean enabled;
}
