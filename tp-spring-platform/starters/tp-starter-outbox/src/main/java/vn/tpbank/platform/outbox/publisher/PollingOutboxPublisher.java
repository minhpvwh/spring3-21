package vn.tpbank.platform.outbox.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import vn.tpbank.platform.outbox.entity.OutboxEvent;
import vn.tpbank.platform.outbox.repository.OutboxRepository;

import java.time.Instant;
import java.util.List;

public class PollingOutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(PollingOutboxPublisher.class);

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public PollingOutboxPublisher(OutboxRepository outboxRepository,
                                  KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${tp.platform.outbox.poll-interval:5000}")
    @Transactional
    public void pollAndPublish() {
        List<OutboxEvent> events = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getAggregateId(), event.getPayload()).get();
                event.setProcessed(true);
                event.setProcessedAt(Instant.now());
                outboxRepository.save(event);
                log.debug("Published outbox event: id={}, topic={}, aggregateId={}",
                        event.getId(), event.getTopic(), event.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to publish outbox event: id={}", event.getId(), e);
                break;
            }
        }
    }
}
