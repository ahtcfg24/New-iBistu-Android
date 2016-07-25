package org.iflab.ibistubydreamfactory.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.iflab.ibistubydreamfactory.utils.CustomJsonDateDeserializer;

import java.util.Date;


/**
 * 用户模型类
 */
public class User extends BaseRecord {
    @JsonProperty("session_token")
    private String sessionToken;

    @JsonProperty("session_id")
    private String sessionId;

    private Long id;

    private String name;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    @JsonProperty("is_sys_admin")
    private Boolean isSysAdmin;

    @JsonProperty("last_login_date")
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    private Date lastLoginData;

    private String host;

    private String role;

    @JsonProperty("role_id")
    private Long roleId;

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getSysAdmin() {
        return isSysAdmin;
    }

    public void setSysAdmin(Boolean sysAdmin) {
        isSysAdmin = sysAdmin;
    }

    public Date getLastLoginData() {
        return lastLoginData;
    }

    public void setLastLoginData(Date lastLoginData) {
        this.lastLoginData = lastLoginData;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "User{" +
                "sessionToken='" + sessionToken + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", isSysAdmin=" + isSysAdmin +
                ", lastLoginData=" + lastLoginData +
                ", host='" + host + '\'' +
                ", role='" + role + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
