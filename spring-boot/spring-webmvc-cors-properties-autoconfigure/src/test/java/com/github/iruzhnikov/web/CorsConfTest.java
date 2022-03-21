package com.github.iruzhnikov.web;

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
