package com.demo.camunda.bpm.extra;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;

public class ExtraTaskDecorator extends TaskDecorator {

    public ExtraTaskDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator.getTaskDefinition(), taskDecorator.getExpressionManager());
    }

    @Override
    protected void initializeTaskAssignee(TaskEntity task, VariableScope variableScope) {
        super.initializeTaskAssignee(task, variableScope);
        // check task assignee is on duty
    }
}
