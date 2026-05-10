package vn.tpbank.platform.observability.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class StandardMetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> commonTags(Environment environment) {
        String appName = environment.getProperty("spring.application.name", "unknown");
        return registry -> registry.config()
                .commonTags("application", appName);
    }
}
