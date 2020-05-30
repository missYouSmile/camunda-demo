package com.demo.camunda.bpm.listener.task;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExtraTaskCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        // TODO: 2020/5/30 complete extra task
        // TODO: 2020/5/30 clear todo
    }
}
