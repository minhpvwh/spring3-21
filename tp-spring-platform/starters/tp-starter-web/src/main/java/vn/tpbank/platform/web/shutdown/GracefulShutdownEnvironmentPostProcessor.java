package vn.tpbank.platform.web.shutdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Seeds graceful-shutdown defaults at the lowest property-source priority so that
 * any value in application.yml or environment variables takes precedence.
 *
 * <p>Defaults injected:
 * <ul>
 *   <li>{@code server.shutdown=graceful} — enables Tomcat/Jetty graceful shutdown
 *   <li>{@code spring.lifecycle.timeout-per-shutdown-phase} — driven by
 *       {@code tp.platform.web.shutdown.timeout-seconds} (default 30 s)
 * </ul>
 */
public class GracefulShutdownEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String SOURCE_NAME = "tp-graceful-shutdown-defaults";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // Read the platform property — may already be set by the service
        String rawTimeout = environment.getProperty("tp.platform.web.shutdown.timeout-seconds", "30");

        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("server.shutdown", "graceful");
        defaults.put("spring.lifecycle.timeout-per-shutdown-phase", rawTimeout + "s");

        // addLast → lowest priority, everything else wins
        if (!environment.getPropertySources().contains(SOURCE_NAME)) {
            environment.getPropertySources().addLast(new MapPropertySource(SOURCE_NAME, defaults));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
