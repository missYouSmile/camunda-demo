package com.demo.camunda.cmd;

import com.demo.camunda.BaseTest;
import com.demo.camunda.helper.BpmnHelper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Slf4j
public class AddCandidateUserCmdTest extends BaseTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testAddCandidate() {
        String processDefinitionKey = "TestProcess";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        String processInstanceId = instance.getId();
        log.info("process instance started with id : {}", processInstanceId);

        // transfer
        handleCurrentTasks(processInstanceId, taskId -> {
            BpmnHelper.execute(new AddCandidateUserCmd(taskId, "1002"));
            return null;
        });

        printActiveTaskCandidateUsers(processInstanceId);
        printProcessState(processInstanceId);

        completeCurrentTasks(processInstanceId, Collections.emptyMap());

        printActiveTaskCandidateUsers(processInstanceId);
        printProcessState(processInstanceId);
    }
}