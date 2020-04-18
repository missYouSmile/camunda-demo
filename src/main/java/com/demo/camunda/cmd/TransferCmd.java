package com.demo.camunda.cmd;

import com.demo.camunda.bpm.extra.ExtraTaskDecorator;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.task.TaskDecorator;
import org.camunda.bpm.engine.impl.util.EnsureUtil;

@Slf4j
public class TransferCmd implements Command<Void> {

    private String toUserId;

    private String taskId;

    public TransferCmd(String toUserId, String taskId) {
        this.toUserId = toUserId;
        this.taskId = taskId;

        EnsureUtil.ensureNotNull("taskId", taskId);
        EnsureUtil.ensureNotNull("toUserId", toUserId);
    }

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
        EnsureUtil.ensureNotNull("task not found with id : " + taskId, "task", task);

        // delete original task
        task.delete("transfer", false);
        ExecutionEntity execution = task.getExecution();
        execution.removeTask(task);

        // new task for toUserId
        ExpressionManager expressionManager = commandContext.getProcessEngineConfiguration().getExpressionManager();
        TaskDecorator taskDecorator = new ExtraTaskDecorator(task.getTaskDefinition(), expressionManager);
        TaskEntity newTask = TaskEntity.createAndInsert(execution);
        taskDecorator.decorate(newTask, execution);
        newTask.setAssignee(toUserId);
        // create historic task instance
        commandContext.getHistoricTaskInstanceManager().createHistoricTask(newTask);
        newTask.fireEvent(TaskListener.EVENTNAME_CREATE);

        return null;
    }
}
