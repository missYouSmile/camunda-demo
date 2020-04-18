package com.demo.camunda.bpm.interceptor;

import com.alibaba.fastjson.JSON;
import com.demo.camunda.bpm.extra.ExtraContext;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandInterceptor;

@Slf4j
public class ExtraContextInterceptor extends CommandInterceptor {
    @Override
    public <T> T execute(Command<T> command) {
        try {
            return next.execute(command);
        } finally {
            log.warn("Handle all messages");
            ExtraContext.handleMessage(msg -> {
                log.info("Send message : {}", JSON.toJSONString(msg));
            });
        }
    }
}
