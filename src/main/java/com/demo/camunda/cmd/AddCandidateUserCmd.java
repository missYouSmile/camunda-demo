package com.demo.camunda.cmd;

import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.impl.util.EnsureUtil;

public class AddCandidateUserCmd implements Command<Void> {

    private String taskId;

    private String candidateUserId;

    public AddCandidateUserCmd(String taskId, String candidateUserId) {
        this.taskId = taskId;
        this.candidateUserId = candidateUserId;

        EnsureUtil.ensureNotNull("taskId", taskId);
        EnsureUtil.ensureNotNull(candidateUserId, candidateUserId);
    }

    @Override
    public Void execute(CommandContext commandContext) {
        TaskEntity task = commandContext.getTaskManager().findTaskById(taskId);
        EnsureUtil.ensureNotNull("task not found with id : " + taskId, "task", task);

        task.addCandidateUser(candidateUserId);
        return null;
    }

}
