package vn.tpbank.service.sample.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.tpbank.platform.web.response.ApiResponse;

/**
 * FeignClient targeting tp-sample-client-service.
 * BearerTokenFeignInterceptor automatically attaches the client_credentials token.
 *
 * The /api/v1/internal/status endpoint requires SCOPE_tf_test — the token
 * obtained for sample_user1 includes this scope (see scripts/get_token.md).
 */
@FeignClient(name = "client-service", url = "${tp.client.client-service.url}")
public interface ClientServiceClient {

    @GetMapping("/api/v1/internal/status")
    ApiResponse<String> status();
}
