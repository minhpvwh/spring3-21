package vn.tpbank.platform.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "tp.platform.security")
public class SecurityProperties {

    private Keycloak keycloak = new Keycloak();
    private Cors cors = new Cors();

    /**
     * Optional path-to-scope rules applied by the auto-configured SecurityFilterChain.
     *
     * Example in application.yml:
     *   tp.platform.security.scope-rules:
     *     - path: /api/v1/internal/**
     *       scope: tf_test
     */
    private List<ScopeRule> scopeRules = new ArrayList<>();

    public Keycloak getKeycloak() { return keycloak; }
    public void setKeycloak(Keycloak keycloak) { this.keycloak = keycloak; }
    public Cors getCors() { return cors; }
    public void setCors(Cors cors) { this.cors = cors; }
    public List<ScopeRule> getScopeRules() { return scopeRules; }
    public void setScopeRules(List<ScopeRule> scopeRules) { this.scopeRules = scopeRules; }

    public static class Keycloak {
        private String issuerUri;
        private String rolesClaim = "realm_access.roles";

        public String getIssuerUri() { return issuerUri; }
        public void setIssuerUri(String issuerUri) { this.issuerUri = issuerUri; }
        public String getRolesClaim() { return rolesClaim; }
        public void setRolesClaim(String rolesClaim) { this.rolesClaim = rolesClaim; }
    }

    public static class Cors {
        private String[] allowedOrigins = {"*"};
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};
        private String[] allowedHeaders = {"*"};
        private boolean allowCredentials = false;
        private long maxAge = 3600;

        public String[] getAllowedOrigins() { return allowedOrigins; }
        public void setAllowedOrigins(String[] allowedOrigins) { this.allowedOrigins = allowedOrigins; }
        public String[] getAllowedMethods() { return allowedMethods; }
        public void setAllowedMethods(String[] allowedMethods) { this.allowedMethods = allowedMethods; }
        public String[] getAllowedHeaders() { return allowedHeaders; }
        public void setAllowedHeaders(String[] allowedHeaders) { this.allowedHeaders = allowedHeaders; }
        public boolean isAllowCredentials() { return allowCredentials; }
        public void setAllowCredentials(boolean allowCredentials) { this.allowCredentials = allowCredentials; }
        public long getMaxAge() { return maxAge; }
        public void setMaxAge(long maxAge) { this.maxAge = maxAge; }
    }

    public static class ScopeRule {
        /** Ant-style path pattern, e.g. /api/v1/internal/** */
        private String path;
        /** Required scope value (without SCOPE_ prefix), e.g. tf_test */
        private String scope;

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
    }
}
