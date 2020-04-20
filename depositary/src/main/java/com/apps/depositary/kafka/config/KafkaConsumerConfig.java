package com.apps.depositary.kafka.config;


import com.apps.depositary.kafka.messaging.ExecutionMessage;
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

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    public static final String GROUPID_DEPOSITARY = "depositary";

    @Value(value = "${kafka.clusterAddress}")
    private String clusterAddress;

    public ConsumerFactory<String, ExecutionMessage> consumerFactory(String groupId, Deserializer keyDeserializer,
                                                                     Deserializer valueDeserializer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, clusterAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    public ConcurrentKafkaListenerContainerFactory<String, ExecutionMessage> kafkaListenerContainerFactory
            (String groupId, Deserializer keyDeserializer, Deserializer valueDeserializer) {
        ConcurrentKafkaListenerContainerFactory<String, ExecutionMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(groupId, keyDeserializer, valueDeserializer));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ExecutionMessage> depositKafkaListenerContainerFactory() {
        return kafkaListenerContainerFactory(GROUPID_DEPOSITARY, new StringDeserializer(), new JsonDeserializer<>(ExecutionMessage.class, false));
    }

}