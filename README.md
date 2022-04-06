# Description

This library will add support of configuration and enabling CORS by application.properties

# for Spring Boot

Add this property to _application.properties_ file for enable/disable reading configuration from file

```properties
spring.mvc.cors.enabled=true
```

For specify details of cors configuration you can use file _cors.yml_ (of course you can use default _
application.properties_ file)

```yml
spring:
  mvc:
    cors:
      mappings: #spring.mvc.cors.mappings.<any_name>.<property>: <value>
        randomName: #just random name, only for mapping
          path: /test/post #ant style path pattern, ATTENTION! not ordered, /** pattern override all other pattern
          #allowed-origins: "*"
          allowed-methods: GET #Enable override all defaults! If disabled: a lot more from all the controller methods included from the path pattern matches
          #allowed-headers: "*"
          #exposed-headers: ('*' - not-supported)
          #allow-credentials: true
          allowed-origin-patterns: .*
          #max-age: PT30M
```

### Autoconfigure for _allowed-methods_ (GET, POST etc.) by _path_ patterns

If you just simple using _@EnableWebMvc_ you must remove it, because it will load automatically by this library

if you need override class

```java 
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
``` 

please change parent class to this or open it for learning how to it works (I mean createRequestMappingHandlerMapping
function)

```java 
import io.github.iruzhnikov.web.servlet.CorsPropWebMvcConfigurationSupport;
``` 

# for Spring Framework

You must register property apply bean

```java
import io.github.iruzhnikov.web.servlet.SpringMvcCorsConfigurer;
```

and you should register autoconfiguration for _allowed-methods_ (not required)

```java
import io.github.iruzhnikov.web.servlet.CorsEndpointHandlerMapping;
```
