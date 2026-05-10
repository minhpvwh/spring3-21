package vn.tpbank.platform.resilience.autoconfigure;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import vn.tpbank.platform.resilience.config.CircuitBreakerDefaults;
import vn.tpbank.platform.resilience.config.RetryDefaults;
import vn.tpbank.platform.resilience.config.RateLimiterDefaults;

@AutoConfiguration
@ConditionalOnClass(CircuitBreakerRegistry.class)
@Import({CircuitBreakerDefaults.class, RetryDefaults.class, RateLimiterDefaults.class})
public class ResilienceAutoConfiguration {
}
