package vn.tpbank.platform.security.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import vn.tpbank.platform.security.audit.SecurityAuditLogger;
import vn.tpbank.platform.security.config.SecurityProperties;
import vn.tpbank.platform.security.keycloak.KeycloakJwtConverter;

import java.util.Arrays;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnClass(SecurityFilterChain.class)
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KeycloakJwtConverter keycloakJwtConverter(SecurityProperties properties) {
        return new KeycloakJwtConverter(properties.getKeycloak().getRolesClaim());
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityAuditLogger securityAuditLogger() {
        return new SecurityAuditLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                    KeycloakJwtConverter jwtConverter,
                                                    SecurityAuditLogger auditLogger,
                                                    SecurityProperties properties) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll();
                // Apply per-path scope rules declared in application.yml
                properties.getScopeRules().forEach(rule ->
                    auth.requestMatchers(rule.getPath()).hasAuthority("SCOPE_" + rule.getScope())
                );
                auth.anyRequest().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter))
            )
            .addFilterAfter(auditLogger, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties properties) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(properties.getCors().getAllowedOrigins()));
        config.setAllowedMethods(Arrays.asList(properties.getCors().getAllowedMethods()));
        config.setAllowedHeaders(Arrays.asList(properties.getCors().getAllowedHeaders()));
        config.setAllowCredentials(properties.getCors().isAllowCredentials());
        config.setMaxAge(properties.getCors().getMaxAge());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
