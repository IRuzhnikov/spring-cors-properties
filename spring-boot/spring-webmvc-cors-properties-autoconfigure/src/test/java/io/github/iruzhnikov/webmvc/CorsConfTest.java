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

package io.github.iruzhnikov.webmvc;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CorsConfTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void testPostCorsMvc() throws Exception {
        assertCors(POST, "/test/post",
                status().isForbidden(), //has broken config
                status().isForbidden()); //has broken config
    }

    @Test
    void testGetWithCorsCorsMvc() throws Exception {
        assertCors(GET, "/test/withCors",
                status().isOk(), //has correct config
                status().isOk()); //has correct config
    }

    @Test
    void testGetSubPathWithCorsCorsMvc() throws Exception {
        assertCors(GET, "/test/withCors/subPath",
                status().isOk(), //has correct config
                status().isOk()); //has correct config
    }

    @Test
    void testGetWithoutCorsCorsMvc() throws Exception {
        assertCors(GET, "/test/withoutCors",
                status().isForbidden(), //has no config
                status().isOk()); //has no config, but this is specific of Spring Security
    }

    private void assertCors(HttpMethod method, String path,
                            ResultMatcher preflightStatusAssertion,
                            ResultMatcher methodStatusAssertion) throws Exception {
        mvc.perform(withCorsHeaders(method, options(path))).andExpect(preflightStatusAssertion);
        mvc.perform(withCorsHeaders(method, request(method, path))).andExpect(methodStatusAssertion);
    }

    @NotNull
    private MockHttpServletRequestBuilder withCorsHeaders(HttpMethod method, MockHttpServletRequestBuilder request) {
        return request
                .header("Access-Control-Request-Method", method.name())
                .header("Origin", "https://www.someurl.com");
    }
}
