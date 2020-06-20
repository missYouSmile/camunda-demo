package com.demo.camunda.cmd;

import com.demo.camunda.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AutoCompleteTaskTest extends BaseTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void testAutoComplete() throws Exception {
        String processDefinitionKey = "TestProcess";
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        String processInstanceId = instance.getId();
        log.info("process instance started with id : {}", processInstanceId);

        while (true) {
            printProcessState(processInstanceId);
            TimeUnit.SECONDS.sleep(5);
        }
    }


}
