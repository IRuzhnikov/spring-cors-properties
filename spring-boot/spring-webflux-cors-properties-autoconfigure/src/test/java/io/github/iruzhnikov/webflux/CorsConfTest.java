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

package io.github.iruzhnikov.webflux;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import java.util.function.Function;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@WithMockUser
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
class CorsConfTest {
    @Autowired
    WebTestClient rest;

    @Test
    void testPostCorsFlux() {
        assertCors(POST, "/test/post",
                StatusAssertions::isForbidden, //has broken config
                StatusAssertions::isForbidden); //has broken config
    }

    @Test
    void testGetWithCorsCorsFlux() {
        assertCors(GET, "/test/withCors",
                StatusAssertions::isOk, //has correct config
                StatusAssertions::isOk); //has correct config
    }

    @Test
    void testGetWithoutCorsCorsFlux() {
        assertCors(GET, "/test/withoutCors",
                StatusAssertions::isForbidden, //has no config
                StatusAssertions::isOk); //has no config, but this is specific of Spring Security
    }

    private void assertCors(HttpMethod method, String path,
                            Function<StatusAssertions, ResponseSpec> preflightStatusAssertion,
                            Function<StatusAssertions, ResponseSpec> methodStatusAssertion) {
        preflightStatusAssertion.apply(withCorsHeaders(method, rest.options().uri(path)).exchange().expectStatus());
        methodStatusAssertion.apply(withCorsHeaders(method, rest.method(method).uri(path)).exchange().expectStatus());
    }

    @NotNull
    private RequestHeadersSpec<?> withCorsHeaders(HttpMethod method, RequestHeadersSpec<?> request) {
        return request
                .header("Access-Control-Request-Method", method.name())
                .header("Origin", "https://www.someurl.com");
    }
}
