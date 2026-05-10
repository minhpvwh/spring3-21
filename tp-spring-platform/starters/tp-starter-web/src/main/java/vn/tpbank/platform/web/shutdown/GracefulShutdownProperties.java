package vn.tpbank.platform.web.shutdown;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Controls graceful shutdown behaviour.
 *
 * <pre>
 * tp:
 *   platform:
 *     web:
 *       shutdown:
 *         timeout-seconds: 30   # max time to wait for in-flight requests
 * </pre>
 *
 * Setting {@code timeout-seconds} also drives {@code spring.lifecycle.timeout-per-shutdown-phase}
 * via {@link GracefulShutdownEnvironmentPostProcessor} at startup.
 */
@ConfigurationProperties(prefix = "tp.platform.web.shutdown")
public class GracefulShutdownProperties {

    /** Maximum seconds to wait for active requests to finish before forcing shutdown. */
    private int timeoutSeconds = 30;

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }
}
