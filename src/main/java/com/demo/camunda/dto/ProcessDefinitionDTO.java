package com.demo.camunda.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProcessDefinitionDTO {
    private String id;
    private String definitionKey;
    private String name;
    private String description;
    private String versionTag;
    private int version;
    private String category;
    private String deploymentId;
}
