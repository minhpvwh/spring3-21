package vn.tpbank.service.client.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tpbank.platform.web.response.ApiResponse;

/**
 * Internal endpoint accessible only to service accounts that present a token
 * with the tf_test scope (enforced by SecurityConfig path rule).
 *
 * Example call from tp-service-template:
 *   GET /api/v1/internal/status  [Authorization: Bearer <client_credentials token>]
 */
@RestController
@RequestMapping("/api/v1/internal")
public class InternalController {

    @GetMapping("/status")
    public ApiResponse<String> status(@AuthenticationPrincipal Jwt jwt) {
        String caller = jwt.getClaimAsString("client_id");
        return ApiResponse.success(
                "tp-sample-client-service is UP — authenticated caller: " + caller);
    }
}
