package com.demo.camunda.bpm.extra;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;

public class ExtraTaskDecorator extends TaskDecorator {

    public ExtraTaskDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator.getTaskDefinition(), taskDecorator.getExpressionManager());
    }

    @Override
    public void decorate(TaskEntity task, VariableScope variableScope) {
        super.decorate(task, variableScope);
        // save extra task
    }

    @Override
    protected void initializeTaskAssignee(TaskEntity task, VariableScope variableScope) {
        super.initializeTaskAssignee(task, variableScope);
        // check task assignee is on duty
    }
}
