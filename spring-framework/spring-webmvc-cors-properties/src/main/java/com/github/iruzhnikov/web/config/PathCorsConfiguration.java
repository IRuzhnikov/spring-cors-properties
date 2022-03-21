package com.github.iruzhnikov.web.config;

import lombok.Data;

import javax.annotation.Nullable;
import java.time.Duration;

@Data
public class PathCorsConfiguration {
    //Default pattern
    public static final String ALL_PATH = "/**";

    private String path;
    @Nullable
    private String[] allowedOrigins;
    @Nullable
    private String[] allowedOriginPatterns;
    @Nullable
    private String[] allowedMethods;
    @Nullable
    private String[] allowedHeaders;
    @Nullable
    private String[] exposedHeaders;
    @Nullable
    private Boolean allowCredentials;
    @Nullable
    private Duration maxAge;
}
