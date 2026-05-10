package vn.tpbank.service.client.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import vn.tpbank.service.client.keycloak.KeycloakTokenService;

/**
 * Feign interceptor that attaches a Keycloak service-account token to every
 * outgoing request using the client_credentials grant.
 */
@Component
public class BearerTokenFeignInterceptor implements RequestInterceptor {

    private final KeycloakTokenService tokenService;

    public BearerTokenFeignInterceptor(KeycloakTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + tokenService.getAccessToken());
    }
}
