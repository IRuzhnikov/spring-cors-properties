# Description

This library enables the CORS configuration in application property 
files such as application.properties / application.yml / cors.yml
In general. no coding is required to configure CORS for different 
environments of your application. All you need to do is 
1. add the feature to your project changing maven / gradle / other build tool config 
2. enable the feature in project property file
3. configure the CORS in the same or separate property file, that's up to you to decide

# How to add the feature
To enable the feature, please add the dependency to your project:
```properties
<dependency>
    <groupId>io.github.iruzhnikov</groupId>
    <artifactId>spring-webmvc-cors-properties-autoconfigure</artifactId>
    <version>0.0.12</version>
</dependency>
```
or 
```properties
implementation("io.github.iruzhnikov:spring-cors-properties:0.0.12")
```

Please change the version to the actual one.

# To enable the feature in Spring Boot project

Add this property to _application.properties_ file for enable/disable reading configuration from file

```properties
spring.web.cors.enabled=true
```

To specify details of CORS configuration you may use a separate file _cors.yml_ (of course you may use default _
application.properties_ file). But please, don't enable the feature (spring.web.cors.enabled=true) 
in a separate cors.yml  Please, use application.properties or application.yml or other main  relevant for 
your application configuration file for that. Spring boot ignores (spring.web.cors.enabled=true) in a 
separate files.

For application.yml files that contains several spring.profiles, please use property file annotation (one line),
otherwise spring boot ignores the config line.

# Configuring the CORS rules
When the feature has been enabled already, please configure the CORS rules:  
```yml
spring:
  web:
    cors:
      mappings: #spring.web.cors.mappings.<any_name>.<property>: <value>
        anyName: #just any name, just for grouping properties under the same path pattern (not used in internal logic)
          path: /path/to/api/ #ant style path pattern, ATTENTION! not ordered, /** pattern override all other pattern
          #allowed-origins: "*"
          allowed-methods: GET #Enable override all defaults! If disabled: a lot more from all the controller methods included from the path pattern matches
          #allowed-headers: "*"
          #exposed-headers: ('*' - not-supported)
          #allow-credentials: true
          allowed-origin-patterns: .*
          #max-age: PT30M
```
The 'randomName' in configuration lets you configure the different groups, e.g. 'path /api' and 'path /api/admin' may 
have CORS configurations. To make the configurations different, please different sections, e.g. 'randomName: api' and
'randomName: apiAdmin'. 

Actually, that's all you need to do basic configuration. The further configs are to let you make tiny 
configurations.

### Autoconfigure for _allowed-methods_ (GET, POST etc.) by _path_ patterns

If you like to have the automatic discovery of the _allowed-methods_ in your CORS configuration, please  
remove _@EnableWebMvc_  in your code and override corresponding class:

```java
import org.springframework.webmvc.servlet.config.annotation.WebMvcConfigurationSupport;
``` 

please change parent class to this or open it for learning how to it works (I mean createRequestMappingHandlerMapping
function)

```java
import io.github.iruzhnikov.webmvc.servlet.CorsPropWebMvcConfigurationSupport;
``` 

# for Spring Framework

You must register property apply bean

```java
import io.github.iruzhnikov.webmvc.servlet.SpringMvcCorsConfigurer;
```

and you should register autoconfiguration for _allowed-methods_ (not required)

```java
import io.github.iruzhnikov.webmvc.servlet.CorsEndpointHandlerMapping;
```
