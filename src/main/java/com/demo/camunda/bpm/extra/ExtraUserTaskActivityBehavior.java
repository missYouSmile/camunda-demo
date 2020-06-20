package com.demo.camunda.bpm.extra;

import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.camunda.bpm.engine.impl.task.TaskDecorator;

public class ExtraUserTaskActivityBehavior extends UserTaskActivityBehavior {

    public ExtraUserTaskActivityBehavior(TaskDecorator taskDecorator) {
        super(taskDecorator);
    }

    @Override
    public void performExecution(ActivityExecution execution) throws Exception {
        super.performExecution(execution);
    }
}
