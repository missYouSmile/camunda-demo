package com.demo.camunda;

import com.demo.camunda.helper.BpmnHelper;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.impl.persistence.entity.TaskEntity;
import org.camunda.bpm.engine.task.IdentityLink;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public abstract class BaseTest {

    private static final ThreadLocal<SimpleDateFormat> FORMATTER_HOLDER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Before
    public void setup() {
        BpmnHelper.deploySource("classpath:bpmn/*.bpmn");
        log.warn("==== BaseTest SETUP ====");
    }

    protected void handleCurrentTasks(String processInstanceId, Function<String, Void> handler) {
        taskService.createTaskQuery()
                .active()
                .processInstanceId(processInstanceId)
                .list()
                .forEach(task -> handler.apply(task.getId()));
    }

    protected void completeCurrentTasks(String processInstanceId, Map<String, Object> variables) {
        taskService.createTaskQuery()
                .active()
                .processInstanceId(processInstanceId)
                .list()
                .forEach(task -> taskService.complete(task.getId(), variables));
    }

    protected void printActiveTaskCandidateUsers(String processInstanceId) {
        BpmnHelper.execute(commandContext -> {
            List<TaskEntity> tasks = commandContext.getTaskManager().findTasksByProcessInstanceId(processInstanceId);
            for (TaskEntity task : tasks) {
                Set<IdentityLink> candidates = task.getCandidates();
                if (CollectionUtils.isEmpty(candidates)) {
                    continue;
                }
                String candidateUserIds = candidates.stream().map(IdentityLink::getUserId)
                        .collect(Collectors.joining(","));
                log.error("printActiveTaskCandidateUsers >>>> {} , {} <<<<", task.getId(), candidateUserIds);
            }
            return null;
        });

    }

    protected void printCurrentTasks(String processInstanceId) {
        log.warn("==== CURRENT TASKS ====");
        historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list()
                .forEach(this::printHistoricTask);
        log.warn("==== CURRENT TASKS ====");
    }

    protected void printProcessState(String processInstanceId) {
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        log.error("printProcessState >>>> {} , {} , {} , {} <<<<", hpi.getId(), dateFormat(hpi.getStartTime()),
                dateFormat(hpi.getEndTime()), hpi.getState());
    }

    private void printHistoricTask(HistoricTaskInstance ht) {
        log.error("printHistoricTask >>>> {} , {} , {} , {}, {} <<<<", ht.getId(), ht.getName(),
                dateFormat(ht.getStartTime()),
                dateFormat(ht.getEndTime()),
                ht.getAssignee());
    }

    private String dateFormat(Date date) {
        if (date == null) {
            return null;
        }
        return FORMATTER_HOLDER.get().format(date);
    }
}
