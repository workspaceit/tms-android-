package com.apper.sarwar.tmsapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUser implements Serializable {

    private String uid;
    private String org_id;
    private String org_slug;
    private String org_name;
    private String name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOrg_id() {
        return org_id;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public String getOrg_slug() {
        return org_slug;
    }

    public void setOrg_slug(String org_slug) {
        this.org_slug = org_slug;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "uid='" + uid + '\'' +
                ", org_id='" + org_id + '\'' +
                ", org_slug='" + org_slug + '\'' +
                ", org_name='" + org_name + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthUser authUser = (AuthUser) o;
        return Objects.equals(uid, authUser.uid) &&
                Objects.equals(org_id, authUser.org_id) &&
                Objects.equals(org_slug, authUser.org_slug) &&
                Objects.equals(org_name, authUser.org_name) &&
                Objects.equals(name, authUser.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, org_id, org_slug, org_name, name);
    }
}
