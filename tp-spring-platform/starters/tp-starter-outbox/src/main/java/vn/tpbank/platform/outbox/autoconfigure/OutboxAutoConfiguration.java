package vn.tpbank.platform.outbox.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import vn.tpbank.platform.outbox.idempotency.IdempotencyFilter;
import vn.tpbank.platform.outbox.idempotency.IdempotencyKeyRepository;
import vn.tpbank.platform.outbox.publisher.PollingOutboxPublisher;
import vn.tpbank.platform.outbox.repository.OutboxRepository;

@AutoConfiguration
@EnableScheduling
@EntityScan(basePackages = "vn.tpbank.platform.outbox")
@EnableJpaRepositories(basePackages = {"vn.tpbank.platform.outbox.repository", "vn.tpbank.platform.outbox.idempotency"})
public class OutboxAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(KafkaTemplate.class)
    public PollingOutboxPublisher pollingOutboxPublisher(OutboxRepository outboxRepository,
                                                         KafkaTemplate<String, String> kafkaTemplate) {
        return new PollingOutboxPublisher(outboxRepository, kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(IdempotencyKeyRepository.class)
    public IdempotencyFilter idempotencyFilter(IdempotencyKeyRepository repository) {
        return new IdempotencyFilter(repository);
    }
}
