package com.apper.sarwar.tmsapp.viewmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganizationForm {
    @JsonProperty("name")
    private String name;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("email")
    private String email;

    @JsonProperty("description")
    private String description;

    public OrganizationForm(String name,String slug,String email,String description){
           this.name=name;
           this.slug=slug;
           this.email=email;
           this.description=description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
