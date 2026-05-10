package vn.tpbank.platform.security.keycloak;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String rolesClaim;
    // Maps the `scope` claim to SCOPE_xxx GrantedAuthority entries
    private final JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

    public KeycloakJwtConverter(String rolesClaim) {
        this.rolesClaim = rolesClaim;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(extractRoleAuthorities(jwt));
        authorities.addAll(scopeConverter.convert(jwt));
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString("preferred_username"));
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRoleAuthorities(Jwt jwt) {
        // Navigate nested claim path like "realm_access.roles"
        String[] parts = rolesClaim.split("\\.");
        Object current = jwt.getClaims();
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<String, Object>) current).get(part);
            } else {
                return Collections.emptyList();
            }
        }
        if (current instanceof List<?> roles) {
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
