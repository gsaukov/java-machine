package com.apps.potok.kafka.producer;

import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class ExecutionMessageProducer {

    @Value(value = "${kafka.topic.executions}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, Execution> executionKafkaTemplate;

    public void sendMessage(Execution message) {

        ListenableFuture<SendResult<String, Execution>> future = executionKafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Execution>>() {

            @Override
            public void onSuccess(SendResult<String, Execution> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendExecutionMessage(Execution message) {
        executionKafkaTemplate.send(topicName, message);
    }
}
