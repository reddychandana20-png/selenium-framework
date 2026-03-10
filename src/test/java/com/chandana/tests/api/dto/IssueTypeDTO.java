package com.chandana.tests.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueTypeDTO {
    private String id;
    private String name;
    private String description;
    private String iconUrl;
    private boolean subtask;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean isSubtask() {
        return subtask;
    }
}
