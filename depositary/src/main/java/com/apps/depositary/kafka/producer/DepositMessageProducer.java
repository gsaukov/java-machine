package com.apps.depositary.kafka.producer;

import com.apps.depositary.kafka.messaging.DepositMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class DepositMessageProducer {

    @Value(value = "${kafka.topic.executions}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, DepositMessage> depositMessageKafkaTemplate;

    public void sendMessage(DepositMessage message) {

        ListenableFuture<SendResult<String, DepositMessage>> future = depositMessageKafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, DepositMessage>>() {

            @Override
            public void onSuccess(SendResult<String, DepositMessage> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendDepositMessage(DepositMessage message) {
        depositMessageKafkaTemplate.send(topicName, message);
    }
}
