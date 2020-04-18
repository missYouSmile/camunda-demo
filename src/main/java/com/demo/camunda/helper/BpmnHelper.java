package com.demo.camunda.helper;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.impl.ServiceImpl;
import org.camunda.bpm.engine.impl.interceptor.Command;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BpmnHelper implements ApplicationContextAware {

    private static CommandExecutor commandExecutor;

    private static RepositoryService repositoryService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        repositoryService = applicationContext.getBean(RepositoryService.class);
        ServiceImpl service = (ServiceImpl) repositoryService;
        commandExecutor = service.getCommandExecutor();
        log.warn("BpmnHelper initialized");
    }

    public static <T> T execute(Command<T> command) {
        return commandExecutor.execute(command);
    }

    public static void deploySource(String resourcePattern) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources;
        try {
            resources = resolver.getResources(resourcePattern);
        } catch (IOException e) {
            log.error("Resource can not found: {}", resourcePattern, e);
            return;
        }

        try {
            DeploymentBuilder builder = repositoryService.createDeployment();
            for (Resource resource : resources) {
                builder.addInputStream(resource.getFilename(), resource.getInputStream());
            }
            builder.deploy();
        } catch (IOException e) {
            throw new RuntimeException("Deploy failed with resource : " + resourcePattern, e);
        }
        log.info("Resource be deployed success");
    }
}
