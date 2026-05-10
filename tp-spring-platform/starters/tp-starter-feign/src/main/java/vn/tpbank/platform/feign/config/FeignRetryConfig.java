package vn.tpbank.platform.feign.config;

import feign.Retryer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRetryConfig {

    @Bean
    @ConditionalOnMissingBean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, 1000, 3);
    }
}
