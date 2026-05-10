package vn.tpbank.platform.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TracingConsumerInterceptor implements ConsumerInterceptor<String, Object> {

    @Override
    public ConsumerRecords<String, Object> onConsume(ConsumerRecords<String, Object> records) {
        records.forEach(record -> {
            Header correlationHeader = record.headers().lastHeader("X-Correlation-ID");
            if (correlationHeader != null) {
                MDC.put("correlationId", new String(correlationHeader.value(), StandardCharsets.UTF_8));
            }
            Header traceHeader = record.headers().lastHeader("X-Trace-ID");
            if (traceHeader != null) {
                MDC.put("traceId", new String(traceHeader.value(), StandardCharsets.UTF_8));
            }
        });
        return records;
    }

    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {}

    @Override
    public void close() {}

    @Override
    public void configure(Map<String, ?> configs) {}
}
