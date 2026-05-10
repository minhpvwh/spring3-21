package vn.tpbank.service.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tpbank.platform.web.response.ApiResponse;
import vn.tpbank.service.client.feign.HelloServiceClient;

/**
 * Demo endpoint that triggers a service-to-service call to the hello service.
 *
 * Flow:
 *   caller → [Bearer token] → GET /api/v1/hello (this service)
 *                              → BearerTokenFeignInterceptor adds client_credentials token
 *                              → GET /api/v1/ping (hello service)
 *                              ← response propagated back to caller
 *
 * Test it (requires a valid Keycloak token — see scripts/get_token.md):
 *   curl -H "Authorization: Bearer <token>" http://localhost:8082/api/v1/hello
 */
@RestController
@RequestMapping("/api/v1")
public class ClientController {

    private final HelloServiceClient helloServiceClient;

    public ClientController(HelloServiceClient helloServiceClient) {
        this.helloServiceClient = helloServiceClient;
    }

    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return helloServiceClient.ping();
    }
}
