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

package io.github.iruzhnikov.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("/withCors")
    public String getWithCors() {
        return "gotit";
    }

    @GetMapping("/withCors/subPath")
    public String getSubPathWithCors() {
        return "gotit";
    }

    @GetMapping("/withoutCors")
    public String getWithoutCors() {
        return "gotit";
    }

    @PostMapping("/post")
    public String post() {
        return "gotit";
    }
}
