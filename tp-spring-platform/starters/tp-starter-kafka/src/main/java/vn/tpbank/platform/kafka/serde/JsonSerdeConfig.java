package vn.tpbank.platform.kafka.serde;

import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES;
import static org.springframework.kafka.support.serializer.JsonDeserializer.USE_TYPE_INFO_HEADERS;

@Configuration
public class JsonSerdeConfig {

    @Bean
    public DefaultKafkaProducerFactoryCustomizer kafkaProducerCustomizer() {
        return factory -> factory.setValueSerializer(new JsonSerializer<>());
    }

    @Bean
    public DefaultKafkaConsumerFactoryCustomizer kafkaConsumerCustomizer() {
        // Configure JSON deserialization via properties to avoid generic type issues
        return factory -> {
            Map<String, Object> configs = new HashMap<>();
            configs.put(TRUSTED_PACKAGES, "vn.tpbank.*");
            configs.put(USE_TYPE_INFO_HEADERS, false);
            factory.updateConfigs(configs);
        };
    }
}
