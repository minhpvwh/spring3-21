package vn.tpbank.platform.kafka.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import vn.tpbank.platform.kafka.serde.JsonSerdeConfig;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@Import(JsonSerdeConfig.class)
public class KafkaAutoConfiguration {
}
