package com.apper.sarwar.tmsapp.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectForm {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("manager")
    private String manager;

    public ProjectForm(String name, String description, String manager) {
        this.name = name;
        this.description = description;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
