package vn.tpbank.service.sample.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import vn.tpbank.service.sample.keycloak.KeycloakTokenService;

/**
 * Attaches a Keycloak client_credentials token to every outgoing Feign request.
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
