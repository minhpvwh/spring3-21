package vn.tpbank.platform.observability.autoconfigure;

import io.micrometer.tracing.Tracer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import vn.tpbank.platform.observability.ObservabilityProperties;
import vn.tpbank.platform.observability.logging.MdcTaskDecorator;
import vn.tpbank.platform.observability.logging.TracingContextFilter;
import vn.tpbank.platform.observability.metrics.StandardMetricsConfig;
import vn.tpbank.platform.observability.tracing.TraceIdProvider;

@AutoConfiguration
@EnableConfigurationProperties(ObservabilityProperties.class)
@Import(StandardMetricsConfig.class)
public class ObservabilityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(Tracer.class)
    public TraceIdProvider traceIdProvider(Tracer tracer) {
        return new TraceIdProvider(tracer);
    }

    @Bean
    @ConditionalOnMissingBean
    public MdcTaskDecorator mdcTaskDecorator() {
        return new MdcTaskDecorator();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Order(Ordered.HIGHEST_PRECEDENCE + 10)
    public TracingContextFilter tracingContextFilter(ObservabilityProperties properties) {
        return new TracingContextFilter(properties.getTracingIdHeader(), properties.getTransactionIdHeader());
    }
}
