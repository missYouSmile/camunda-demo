package com.demo.camunda.deployment;

import com.demo.camunda.dto.ProcessDefinitionDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProcessDefinitionService {

    /**
     * the file of process definition
     *
     * @param file 流程定义文件
     */
    void deploy(MultipartFile file);

    /**
     * list all of the process definition
     *
     * @return list of process definition
     */
    List<ProcessDefinitionDTO> list();

}
