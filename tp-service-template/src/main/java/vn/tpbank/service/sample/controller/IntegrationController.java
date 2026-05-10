package vn.tpbank.service.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tpbank.platform.web.response.ApiResponse;
import vn.tpbank.service.sample.feign.ClientServiceClient;

/**
 * Demonstrates a service-to-service call from tp-service-template
 * to the scope-protected endpoint on tp-sample-client-service.
 *
 * Flow:
 *   caller → [Bearer token] → GET /api/v1/integration/status (this service, port 8081)
 *     → BearerTokenFeignInterceptor fetches client_credentials token from Keycloak
 *     → GET /api/v1/internal/status [Authorization: Bearer <token with SCOPE_tf_test>]
 *       → tp-sample-client-service (port 8082) validates scope and responds
 *
 * Test:
 *   TOKEN=$(curl -s -X POST http://localhost:8080/realms/tp-internal/protocol/openid-connect/token \
 *     -d 'grant_type=client_credentials&client_id=sample_user1&client_secret=3Sdqr4igeEWs2AtsgJz4QjN1kslXjGiT' \
 *     | jq -r .access_token)
 *   curl -H "Authorization: Bearer $TOKEN" http://localhost:8081/api/v1/integration/status
 */
@RestController
@RequestMapping("/api/v1/integration")
public class IntegrationController {

    private final ClientServiceClient clientServiceClient;

    public IntegrationController(ClientServiceClient clientServiceClient) {
        this.clientServiceClient = clientServiceClient;
    }

    @GetMapping("/status")
    public ApiResponse<String> status() {
        return clientServiceClient.status();
    }
}
