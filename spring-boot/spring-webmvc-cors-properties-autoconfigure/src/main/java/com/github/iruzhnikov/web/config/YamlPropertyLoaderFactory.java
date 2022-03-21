package com.github.iruzhnikov.web.config;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Check PropertySource support or
 * StandardConfigDataLocationResolver#configNames check add custom names
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        List<PropertySource<?>> propertySources =
                new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        return propertySources.get(0);
    }
}
