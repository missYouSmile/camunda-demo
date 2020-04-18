package com.demo.camunda.deployment.impl;

import com.demo.camunda.deployment.ProcessDefinitionService;
import com.demo.camunda.dto.ProcessDefinitionDTO;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deploy(MultipartFile file) {
        DeploymentBuilder builder = repositoryService.createDeployment();
        try {
            builder.addInputStream(file.getOriginalFilename(), file.getInputStream());
        } catch (IOException e) {
            log.error("add deployment resource failed! {}", file.getName(), e);
        }
        builder.deploy();
    }

    @Override
    public List<ProcessDefinitionDTO> list() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .active()
                .list();
        return list.stream()
                .map(e -> new ProcessDefinitionDTO()
                        .setId(e.getId())
                        .setDefinitionKey(e.getKey())
                        .setName(e.getName())
                        .setDescription(e.getDescription())
                        .setCategory(e.getCategory())
                        .setVersion(e.getVersion())
                        .setVersionTag(e.getVersionTag())
                        .setDeploymentId(e.getDeploymentId()))
                .collect(Collectors.toList());
    }
}
