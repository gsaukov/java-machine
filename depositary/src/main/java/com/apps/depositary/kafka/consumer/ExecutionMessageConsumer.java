package com.apps.depositary.kafka.consumer;

import com.apps.depositary.kafka.messaging.ExecutionMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import static com.apps.depositary.kafka.config.KafkaConsumerConfig.GROUPID_DEPOSITARY;

@Component
public class ExecutionMessageConsumer {

    public final AtomicLong i = new AtomicLong();

    @KafkaListener(topics = "${kafka.topic.executions}", groupId = GROUPID_DEPOSITARY, containerFactory = "depositKafkaListenerContainerFactory")
    public void listenGroupDeposit(ExecutionMessage message) {
        if(i.incrementAndGet() > 999 && i.get() % 1000 == 0) {
            System.out.println("Received Messasge #" + i.get() + " in groupid 'deposit': " + message);
        }

    }

}
