package com.demo.camunda.bpm.extra;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;
import org.camunda.bpm.engine.impl.task.TaskDefinition;

public class TransferTaskDecorator extends TaskDecorator {

    private final String transferUserId;

    public TransferTaskDecorator(TaskDefinition taskDefinition, ExpressionManager expressionManager, String transferUserId) {
        super(taskDefinition, expressionManager);
        this.transferUserId = transferUserId;
        Preconditions.checkArgument(Strings.isNullOrEmpty(transferUserId), "transferUserId is null");
    }

    @Override
    protected void initializeTaskAssignee(TaskEntity task, VariableScope variableScope) {
        task.setAssignee(transferUserId);
    }
}
