package com.demo.camunda.config;

import com.demo.camunda.bpm.custom.CustomProcessEngineConfiguration;
import com.demo.camunda.bpm.extra.ExtraTaskDecorator;
import com.demo.camunda.bpm.listener.task.ExtraTaskAssignmentListener;
import com.demo.camunda.bpm.listener.task.ExtraTaskCompleteListener;
import com.demo.camunda.bpm.listener.task.ExtraTaskCreateListener;
import com.demo.camunda.bpm.listener.task.ExtraTaskDeleteListener;
import com.google.common.collect.Lists;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.pvm.process.ScopeImpl;
import org.camunda.bpm.engine.impl.task.TaskDecorator;
import org.camunda.bpm.engine.impl.task.TaskDefinition;
import org.camunda.bpm.engine.impl.util.xml.Element;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class CamundaConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration() {
        CustomProcessEngineConfiguration config = new CustomProcessEngineConfiguration();
        config.setProcessEngineName("camunda-demo");
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setHistory(ProcessEngineConfiguration.HISTORY_AUDIT);
        config.setJobExecutorActivate(true);

        config.setCustomPostBPMNParseListeners(postParseListeners());
        return config;
    }

    private List<BpmnParseListener> postParseListeners() {
        AbstractBpmnParseListener postParseListeners = new AbstractBpmnParseListener() {
            @Override
            public void parseUserTask(Element userTaskElement, ScopeImpl scope, ActivityImpl activity) {
                replaceUserTaskActivityBehavior(activity);
            }
        };
        return Lists.newArrayList(postParseListeners);
    }

    private void replaceUserTaskActivityBehavior(ActivityImpl activity) {
        UserTaskActivityBehavior activityBehavior = buildUserTaskActivityBehavior(activity);
        activity.setActivityBehavior(activityBehavior);
    }

    private UserTaskActivityBehavior buildUserTaskActivityBehavior(ActivityImpl activity) {
        UserTaskActivityBehavior behavior = (UserTaskActivityBehavior) activity.getActivityBehavior();
        TaskDecorator originalTaskDecorator = behavior.getTaskDecorator();
        decorateTaskDefinition(originalTaskDecorator);
        ExtraTaskDecorator realTaskDecorator = new ExtraTaskDecorator(originalTaskDecorator);
        return new UserTaskActivityBehavior(realTaskDecorator);
    }

    private void decorateTaskDefinition(TaskDecorator originalTaskDecorator) {
        TaskDefinition taskDefinition = originalTaskDecorator.getTaskDefinition();
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_CREATE, new ExtraTaskCreateListener());
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_ASSIGNMENT, new ExtraTaskAssignmentListener());
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_COMPLETE, new ExtraTaskCompleteListener());
        taskDefinition.addTaskListener(TaskListener.EVENTNAME_DELETE, new ExtraTaskDeleteListener());
    }

    @Bean
    public ProcessEngineFactoryBean processEngine() {
        ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
        factoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return factoryBean;
    }

    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }
}
