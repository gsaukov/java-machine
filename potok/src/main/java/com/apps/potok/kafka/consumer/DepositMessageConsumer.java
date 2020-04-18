package com.apps.potok.kafka.consumer;

import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.kafka.config.KafkaConsumerConfig.GROUPID_DEPOSIT;

@Component
public class DepositMessageConsumer {

    public final AtomicLong i = new AtomicLong();

    @KafkaListener(topics = "${kafka.topic.executions}", groupId = GROUPID_DEPOSIT, containerFactory = "depositKafkaListenerContainerFactory")
    public void listenGroupDeposit(Execution message) {
        if(i.incrementAndGet() > 999 && i.get() % 1000 == 0) {
            System.out.println("Received Messasge #" + i.get() + " in groupid 'deposit': " + message);
        }

    }

}
