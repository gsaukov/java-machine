package com.apps.potok.kafka.consumer;

import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.apps.potok.kafka.config.KafkaConsumerConfig.GROUPID_DEPOSIT;

@Component
public class DepositMessageConsumer {

    @KafkaListener(topics = "${kafka.topic.executions}", groupId = GROUPID_DEPOSIT, containerFactory = "depositKafkaListenerContainerFactory")
    public void listenGroupDeposit(Execution message) {
        System.out.println("Received Messasge in groupid 'deposit': " + message);
    }

}
