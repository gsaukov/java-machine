package com.apps.potok.kafka.config;

import java.util.HashMap;
import java.util.Map;

import com.apps.potok.soketio.model.execution.Deposit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    public static final String GROUPID_POTOK = "POTOK";

    @Value(value = "${kafka.clusterAddress}")
    private String clusterAddress;

    public ConsumerFactory<String, Deposit> consumerFactory(String groupId, Deserializer keyDeserializer,
                                                              Deserializer valueDeserializer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 180000);
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    public ConcurrentKafkaListenerContainerFactory<String, Deposit> kafkaListenerContainerFactory
            (String groupId, Deserializer keyDeserializer, Deserializer valueDeserializer) {
        ConcurrentKafkaListenerContainerFactory<String, Deposit> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId, keyDeserializer, valueDeserializer));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Deposit> depositKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(GROUPID_POTOK, new StringDeserializer(), new JsonDeserializer<>(Deposit.class));
    }

}