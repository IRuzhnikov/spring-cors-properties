# Description

This library enables the CORS configuration in application property
files such as application.properties / application.yml / cors.yml
In general. no coding is required to configure CORS for different
environments of your application. All you need to do is

1. add the feature to your project changing maven / gradle / other build tool config
2. enable the feature in project property file
3. configure the CORS in the same or separate property file, that's up to you to decide

# How to add the feature to Spring Boot

To enable the feature, please add the dependency to your project (navigate to this links):  
[![MVC](https://img.shields.io/maven-central/v/io.github.iruzhnikov/spring-webmvc-cors-properties-autoconfigure.svg?label=Maven%20Central:%20Spring%20MVC.&style=flat-square)](https://search.maven.org/search?q=g:%22io.github.iruzhnikov%22%20AND%20a:%22spring-webmvc-cors-properties-autoconfigure%22)  
group: _io.github.iruzhnikov_  
name: _spring-webmvc-cors-properties-autoconfigure_  
[![FLUX](https://img.shields.io/maven-central/v/io.github.iruzhnikov/spring-webflux-cors-properties-autoconfigure.svg?label=Maven%20Central:%20Spring%20FLUX&style=flat-square)](https://search.maven.org/search?q=g:%22io.github.iruzhnikov%22%20AND%20a:%22spring-webflux-cors-properties-autoconfigure%22)  
group: _io.github.iruzhnikov_  
name: _spring-webflux-cors-properties-autoconfigure_

# To enable the feature in Spring Boot project

Add this property to `application.properties` file for adding reading configuration from different file

`application.properties` file content:

```yml
spring.config.import: optional:classpath:cors.yml
```

Configure the CORS rules in `cors.yml` properties file

`cors.yml` file content:

```yml
spring:
  web:
    cors:
      enabled: true
      mappings: #spring.web.cors.mappings.<any_name>.<property>: <value>
        anyName: #just any name, just for grouping properties under the same path pattern (not used in internal logic)
          paths: #ant style path pattern, ATTENTION! not ordered, /** pattern override all other pattern
            - /path/to/api
            - /path/to/api/**
          #allowed-origins: "*"
          allowed-methods: GET #Enable override all defaults! If disabled: a lot more from all the controller methods included from the path pattern matches
          #allowed-headers: "*"
          #exposed-headers: ('*' - not-supported)
          #allow-credentials: true
          allowed-origin-patterns: .*
          #max-age: PT30M
```

The 'anyName' in configuration lets you configure the different groups, e.g. 'path /api' and 'path /api/admin' may
have CORS configurations. To make the configurations different, please different sections, e.g. 'anyName: api' and
'anyName: apiAdmin'.

Actually, that's all you need to do basic configuration. The further configs are to let you make tiny
configurations.

> ![](https://img.shields.io/static/v1?label=&message=NOTE&style=flat-square&color=blue)  
> To specify details of CORS configuration you may use a separate file _cors.yml_ (of course you may use default
> _application.properties_ file).  
> [more details about `spring.config.import` in Spring](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#config-data-import)

> ![](https://img.shields.io/static/v1?label=&message=WARNING&style=flat-square&color=orange)  
> Don't use this library for CORS configuration for `Spring Actuator`,
> because this library has self specific config (props name like of _management.*_)  
> [more details in Spring](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.cors)

> ![](https://img.shields.io/static/v1?label=&message=WARNING&style=flat-square&color=orange)  
> For application.yml files that contains several 'spring.profiles', please use property file annotation (one line)
> for `spring.web.cors.enabled`, otherwise spring boot ignores the config line.  
> [more details in stackoverflow](https://stackoverflow.com/a/35400025)

### Autoconfigure for _allowed-methods_ (GET, POST etc.) by _path_ patterns

If you like to have the automatic discovery of the _allowed-methods_ in your CORS configuration, please  
remove _@EnableWebMvc_  in your code.

> ![](https://img.shields.io/static/v1?label=&message=ATTENTION&style=flat-square&color=red)  
> if you extended corresponding class:
> ```java
> //for MVC
> import org.springframework.webmvc.servlet.config.annotation.WebMvcConfigurationSupport;
> //for FLUX
> import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
> ``` 
>
> please change extended class to next class or open it for learning how to it works  
> (I mean createRequestMappingHandlerMapping
> function)
>
> ```java
> //for MVC
> import io.github.iruzhnikov.webmvc.servlet.CorsPropWebMvcConfigurationSupport;
> //for FLUX
> import io.github.iruzhnikov.webflux.servlet.CorsPropWebFluxConfigurationSupport;
> ``` 

# To enable the feature in Spring Framework

You must register property apply bean

```java
//for MVC
import io.github.iruzhnikov.webmvc.servlet.SpringMvcCorsConfigurer;
//for FLUX
import io.github.iruzhnikov.webflux.servlet.SpringFluxCorsConfigurer;
```

and you should register autoconfiguration for _allowed-methods_ (not required)

```java
//for MVC
import io.github.iruzhnikov.webmvc.servlet.CorsEndpointHandlerMapping;
//for FLUX
import io.github.iruzhnikov.webflux.servlet.CorsEndpointHandlerMapping;
```
