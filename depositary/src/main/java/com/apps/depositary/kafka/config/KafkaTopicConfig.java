package com.apps.depositary.kafka.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.clusterAddress}")
    private String clusterAddress;

    @Value(value = "${kafka.topic.executions}")
    private String topicExecutions;

    @Value(value = "${kafka.topic.deposits}")
    private String topicDeposits;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, clusterAddress);
        KafkaAdmin kafkaAdmin = new KafkaAdmin(configs);
        return kafkaAdmin;
    }

    @Bean
    public NewTopic executionsTopic() {
        return new NewTopic(topicExecutions, 1, (short) 1);
    }

    @Bean
    public NewTopic depositsTopic() {
        return new NewTopic(topicDeposits, 1, (short) 1);
    }

}