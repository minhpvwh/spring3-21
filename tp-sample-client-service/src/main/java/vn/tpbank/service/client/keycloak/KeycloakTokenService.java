package vn.tpbank.service.client.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;

/**
 * Obtains and caches a service account token from Keycloak using the
 * client_credentials grant. Token is refreshed automatically 30 s before expiry.
 *
 * curl equivalent (see scripts/get_token.md):
 *   POST /realms/tp-internal/protocol/openid-connect/token
 *   grant_type=client_credentials & client_id=... & client_secret=...
 */
@Service
public class KeycloakTokenService {

    private final RestClient restClient;
    private final KeycloakClientProperties properties;

    private volatile String cachedToken;
    private volatile Instant tokenExpiry = Instant.EPOCH;

    public KeycloakTokenService(RestClient.Builder builder, KeycloakClientProperties properties) {
        this.restClient = builder.build();
        this.properties = properties;
    }

    public synchronized String getAccessToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiry.minusSeconds(30))) {
            TokenResponse response = fetchToken();
            cachedToken = response.accessToken();
            tokenExpiry = Instant.now().plusSeconds(response.expiresIn());
        }
        return cachedToken;
    }

    private TokenResponse fetchToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());

        return restClient.post()
                .uri(properties.getTokenUri())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);
    }

    record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") long expiresIn
    ) {}
}
