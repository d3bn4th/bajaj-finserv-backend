package com.bajaj.webhookapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class InitialResponse {
    private String webhookUrl;
    private String accessToken;
    private Map<String, Object> problem;

    // For problem 1 (Mutual Followers)
    private List<List<Integer>> followers;

    // For problem 2 (Nth-Level Followers)
    private Integer userId;
    private Integer n;

    public InitialResponse() {
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("problem")
    private void unpackProblem(Map<String, Object> problem) {
        this.problem = problem;

        if (problem.containsKey("followers")) {
            this.followers = (List<List<Integer>>) problem.get("followers");
        }

        if (problem.containsKey("userId")) {
            this.userId = (Integer) problem.get("userId");
        }

        if (problem.containsKey("n")) {
            this.n = (Integer) problem.get("n");
        }
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Map<String, Object> getProblem() {
        return problem;
    }

    public void setProblem(Map<String, Object> problem) {
        this.problem = problem;
    }

    public List<List<Integer>> getFollowers() {
        return followers;
    }

    public void setFollowers(List<List<Integer>> followers) {
        this.followers = followers;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }
}