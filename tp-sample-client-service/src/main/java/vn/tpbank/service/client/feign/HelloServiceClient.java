package vn.tpbank.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import vn.tpbank.platform.web.response.ApiResponse;

/**
 * FeignClient targeting the hello/sample service.
 * URL is resolved from application.yml: tp.client.hello-service.url
 *
 * The BearerTokenFeignInterceptor automatically adds the Keycloak
 * service-account token to every request made by this client.
 */
@FeignClient(name = "hello-service", url = "${tp.client.hello-service.url}")
public interface HelloServiceClient {

    @GetMapping("/api/v1/ping")
    ApiResponse<String> ping();
}
