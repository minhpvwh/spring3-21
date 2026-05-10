package vn.tpbank.service.sample.keycloak;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.time.Instant;

/**
 * Obtains and caches a Keycloak service-account token via client_credentials grant.
 * Token is refreshed 30s before expiry.
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
