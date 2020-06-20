package com.demo.camunda.bpm.custom;

import com.demo.camunda.bpm.extra.ExtraContext;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandInvocationContext;

public class CustomContext extends CommandContext {

    public CustomContext(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    @Override
    public void close(CommandInvocationContext commandInvocationContext) {
        try {
            super.close(commandInvocationContext);
        } finally {
            ExtraContext.close(commandInvocationContext);
        }
    }
}
