package com.samdev.customizedOauth20.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String username;
    private String email;
    private String password;
    private Integer statusCode;
    private String responseMessage;
    private Integer otp;
    private String token;
    private String role;

    public UserDTO() {
    }

    public UserDTO(String username, String email, String password, Integer statusCode, String responseMessage, String token, Integer otp, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.token = token;
        this.otp = otp;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
