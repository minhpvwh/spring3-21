package vn.tpbank.service.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tpbank.platform.web.response.ApiResponse;

@RestController
@RequestMapping("/api/v1")
public class HealthCheckController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.success("pong");
    }
}
