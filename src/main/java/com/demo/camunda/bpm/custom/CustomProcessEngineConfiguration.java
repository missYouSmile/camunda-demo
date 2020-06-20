package com.demo.camunda.bpm.custom;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;

public class CustomProcessEngineConfiguration extends SpringProcessEngineConfiguration {

    @Override
    protected void initCommandContextFactory() {
        commandContextFactory = new CustomCommandContextFactory();
        commandContextFactory.setProcessEngineConfiguration(this);
    }
}
