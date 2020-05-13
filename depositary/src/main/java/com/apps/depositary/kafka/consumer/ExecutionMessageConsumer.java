package com.apps.depositary.kafka.consumer;

import com.apps.depositary.kafka.messaging.ExecutionMessage;
import com.apps.depositary.persistance.entity.Execution;
import com.apps.depositary.persistance.repository.ExecutionRepository;
import com.apps.depositary.service.DepositaryService;
import com.apps.depositary.service.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.depositary.kafka.config.KafkaConsumerConfig.GROUPID_DEPOSITARY;

@Component
public class ExecutionMessageConsumer implements ConsumerSeekAware {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    @Value("${kafka.topic.executions}")
    private String executionsTopic;

    private final AtomicLong i = new AtomicLong();
    private ArrayList<Execution> messageBatch = new ArrayList<>();

    private ConsumerSeekCallback callback;

    @Autowired
    private ExecutionRepository executionRepository;

    @Autowired
    private DepositaryService depositaryService;

    @KafkaListener(topics = "${kafka.topic.executions}", groupId = GROUPID_DEPOSITARY, containerFactory = "depositKafkaListenerContainerFactory")
    public void listenGroupDeposit(@Payload ExecutionMessage message,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                   @Header(KafkaHeaders.OFFSET) int offset,
                                   Acknowledgment acknowledgment) {

        Execution execution = toExecution(message);
        depositaryService.processExecution(execution);
        messageBatch.add(execution);

        if(i.incrementAndGet() >= batchSize && i.get() % batchSize == 0) {
            executionRepository.saveAll(messageBatch);
            messageBatch = new ArrayList<>();
        }
        //TODO offset should persisted as well to track last success
        //So it would be possible to setOffset for retry or replay.
        acknowledgment.acknowledge();
    }

    private Execution toExecution(ExecutionMessage message) {
        Execution execution = new Execution();
        execution.setUuid(message.getUuid());
        execution.setCounterExecutionUuid(message.getCounterExecutionUuid());
        execution.setOrderUuid(message.getOrderUuid());
        execution.setTimestamp(message.getTimestamp());
        execution.setSymbol(message.getSymbol());
        execution.setAccountId(message.getAccountId());
        execution.setRoute(Route.valueOf(message.getRoute()));
        execution.setFillPrice(message.getFillPrice());
        execution.setBlockedPrice(message.getBlockedPrice());
        execution.setQuantity(message.getQuantity());
        execution.setFilled(message.isFilled());
        return execution;
    }

    private void setOffset(int partition, long newOffset) {
        callback.seek(executionsTopic, partition, newOffset);
    }

    @Override
    public void registerSeekCallback(ConsumerSeekCallback callback) {
        this.callback = callback;
    }
}
