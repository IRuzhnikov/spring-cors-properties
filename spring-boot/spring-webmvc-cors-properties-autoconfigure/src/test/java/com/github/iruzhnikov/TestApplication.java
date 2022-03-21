package com.github.iruzhnikov;

import com.github.iruzhnikov.web.servlet.CorsPropWebMvcConfigurationSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@EnableWebSecurity
@AutoConfigurationPackage(basePackageClasses = CorsPropWebMvcConfigurationSupport.class)
@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(lazyInit = true)
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }
}
