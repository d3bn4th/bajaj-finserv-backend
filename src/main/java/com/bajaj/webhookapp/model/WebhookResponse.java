package com.bajaj.webhookapp.model;

import java.util.List;

public class WebhookResponse {
    private String name;
    private String regNo;
    private String email;
    private List<?> result;

    public WebhookResponse() {
    }

    public WebhookResponse(String name, String regNo, String email, List<?> result) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }
}