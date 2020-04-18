package com.demo.camunda.controller;

import com.demo.camunda.deployment.ProcessDefinitionService;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/process-definition")
public class ProcessDefinitionController {

    @Autowired
    private ProcessDefinitionService processDefinitionService;

    @PostMapping
    public void deploy(MultipartFile file) {
        Preconditions.checkArgument(file != null, "file is null");
        processDefinitionService.deploy(file);
    }

    @GetMapping
    public Object list() {
        return processDefinitionService.list();
    }
}
