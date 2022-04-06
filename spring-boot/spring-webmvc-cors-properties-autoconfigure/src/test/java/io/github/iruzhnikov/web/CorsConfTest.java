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

package io.github.iruzhnikov.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser("spring")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CorsConfTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void testPostCorsMvc() throws Exception {
        mvc.perform(options("/test/post")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://www.someurl.com"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetWithCorsCorsMvc() throws Exception {
        mvc.perform(options("/test/withCors")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://www.someurl.com"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetWithoutCorsCorsMvc() throws Exception {
        mvc.perform(options("/test/withoutCors")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://www.someurl.com"))
                .andExpect(status().isForbidden());
    }
}
