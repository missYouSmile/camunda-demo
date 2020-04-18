package com.demo.camunda.bpm.mq;

import lombok.Data;

@Data
public class MqMessage {

    private String routingKey;

}
