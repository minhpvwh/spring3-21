package vn.tpbank.platform.data.oracle.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Class<?> secCtxClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
            Object ctx = secCtxClass.getMethod("getContext").invoke(null);
            Object auth = ctx.getClass().getMethod("getAuthentication").invoke(ctx);
            if (auth != null) {
                boolean isAuth = (boolean) auth.getClass().getMethod("isAuthenticated").invoke(auth);
                if (isAuth) {
                    String name = (String) auth.getClass().getMethod("getName").invoke(auth);
                    if (name != null && !name.equals("anonymousUser")) {
                        return Optional.of(name);
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return Optional.of("system");
    }
}
