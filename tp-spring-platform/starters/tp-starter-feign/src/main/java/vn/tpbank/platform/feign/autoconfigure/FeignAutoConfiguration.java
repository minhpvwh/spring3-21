package vn.tpbank.platform.feign.autoconfigure;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import vn.tpbank.platform.feign.config.FeignRetryConfig;
import vn.tpbank.platform.feign.decoder.StandardErrorDecoder;
import vn.tpbank.platform.feign.interceptor.TraceIdRequestInterceptor;
import vn.tpbank.platform.observability.ObservabilityProperties;

@AutoConfiguration
@ConditionalOnClass(RequestInterceptor.class)
@Import(FeignRetryConfig.class)
public class FeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "traceIdRequestInterceptor")
    public TraceIdRequestInterceptor traceIdRequestInterceptor(ObservabilityProperties properties) {
        return new TraceIdRequestInterceptor(properties.getTracingIdHeader(), properties.getTransactionIdHeader());
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder standardErrorDecoder() {
        return new StandardErrorDecoder();
    }
}
