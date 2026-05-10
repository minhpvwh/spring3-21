package vn.tpbank.platform.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.util.backoff.FixedBackOff;

public class DltErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(DltErrorHandler.class);

    public static CommonErrorHandler create(KafkaOperations<?, ?> kafkaOperations, long backoffInterval, long maxAttempts) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaOperations);
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(backoffInterval, maxAttempts));
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.warn("Kafka retry attempt {} for topic={}, partition={}, offset={}",
                        deliveryAttempt, record.topic(), record.partition(), record.offset()));
        return handler;
    }
}
