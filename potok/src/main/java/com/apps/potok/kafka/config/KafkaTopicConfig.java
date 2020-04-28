package com.apps.potok.kafka.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

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
        KafkaAdmin kafkaAdmin = new KafkaAdmin(getConfigs());
        return kafkaAdmin;
    }

    @Bean
    public AdminClient adminClient() {
        AdminClient adminClient = AdminClient.create(getConfigs());
//        adminClient.deleteTopics(Arrays.asList("executions"));
        return adminClient;
    }

    @Bean
    public NewTopic depositsTopic() {
        return new NewTopic(topicDeposits, 1, (short) 1);
    }

    private Map<String, Object> getConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, clusterAddress);
        return configs;
    }
}