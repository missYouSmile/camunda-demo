package com.demo.camunda.bpm.extra;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.Expression;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;

@Slf4j
public class ExtraTaskDecorator extends TaskDecorator {

    public ExtraTaskDecorator(TaskDecorator taskDecorator) {
        super(taskDecorator.getTaskDefinition(), taskDecorator.getExpressionManager());
    }

    @Override
    protected void initializeTaskAssignee(TaskEntity task, VariableScope variableScope) {
        Expression assigneeExpression = taskDefinition.getAssigneeExpression();
        if (assigneeExpression != null) {
            String assignee = (String) assigneeExpression.getValue(variableScope);
            task.setAssignee(assignee);
            if (Strings.isNullOrEmpty(assignee)) {
                log.warn("No assignee found for task : [{}]{}", task.getName(), task.getId());
                // Add to AutoCompleteTask when No assignee found
                ExtraContext.addAutoCompleteTask(task.getId());
            }
        }
    }
}
