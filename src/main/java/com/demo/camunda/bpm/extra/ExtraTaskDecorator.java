package com.demo.camunda.bpm.extra;

import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;
import org.camunda.bpm.engine.impl.task.TaskDefinition;

public class ExtraTaskDecorator extends TaskDecorator {

    public ExtraTaskDecorator(TaskDefinition taskDefinition, ExpressionManager expressionManager) {
        super(taskDefinition, expressionManager);
    }

    @Override
    public void decorate(TaskEntity task, VariableScope variableScope) {
        // set the taskDefinition
        task.setTaskDefinition(taskDefinition);
        
        // name
        initializeTaskName(task, variableScope);
        // description
        initializeTaskDescription(task, variableScope);
        // dueDate
        initializeTaskDueDate(task, variableScope);
        // followUpDate
        initializeTaskFollowUpDate(task, variableScope);
        // priority
        initializeTaskPriority(task, variableScope);
    }
}
