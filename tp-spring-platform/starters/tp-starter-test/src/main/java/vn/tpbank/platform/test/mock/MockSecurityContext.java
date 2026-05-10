package vn.tpbank.platform.test.mock;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "test-user", roles = {"USER"})
public @interface MockSecurityContext {
    String username() default "test-user";
    String[] roles() default {"USER"};
}
