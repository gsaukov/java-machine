package com.apps.depositary.kafka.consumer;

import com.apps.depositary.kafka.messaging.ExecutionMessage;
import com.apps.depositary.persistance.entity.Execution;
import com.apps.depositary.persistance.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.depositary.kafka.config.KafkaConsumerConfig.GROUPID_DEPOSITARY;

@Component
public class ExecutionMessageConsumer {

    private final AtomicLong i = new AtomicLong();
    private ArrayList<Execution> messageBatch = new ArrayList<>();

    @Autowired
    private ExecutionRepository executionRepository;

    @KafkaListener(topics = "${kafka.topic.executions}", groupId = GROUPID_DEPOSITARY, containerFactory = "depositKafkaListenerContainerFactory")
    public void listenGroupDeposit(ExecutionMessage message) {
        if(i.incrementAndGet() > 10000 && i.get() % 10000 == 0) {
            messageBatch.add(toExecution(message));
            executionRepository.saveAll(messageBatch);
            messageBatch = new ArrayList<>();
        } else {
            messageBatch.add(toExecution(message));
        }
    }

    private Execution toExecution(ExecutionMessage message) {
        Execution execution = new Execution();
        execution.setUuid(message.getUuid());
        execution.setCounterExecutionUuid(message.getCounterExecutionUuid());
        execution.setOrderUuid(message.getOrderUuid());
        execution.setTimestamp(message.getTimestamp());
        execution.setSymbol(message.getSymbol());
        execution.setAccountId(message.getAccountId());
        execution.setRoute(message.getRoute());
        execution.setFillPrice(message.getFillPrice());
        execution.setBlockedPrice(message.getBlockedPrice());
        execution.setQuantity(message.getQuantity());
        execution.setFilled(message.isFilled());
        return execution;
    }


}
