package com.qhelpme.models;

import java.util.UUID;

public class HelperStats {

    private final UUID helperUUID;
    private String helperName;
    private int accepts;
    private int rejects;

    public HelperStats(UUID helperUUID, String helperName) {
        this.helperUUID = helperUUID;
        this.helperName = helperName;
    }

    public UUID getHelperUUID() {
        return helperUUID;
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public int getAccepts() {
        return accepts;
    }

    public void incrementAccepts() {
        accepts++;
    }

    public void setAccepts(int accepts) {
        this.accepts = accepts;
    }

    public int getRejects() {
        return rejects;
    }

    public void incrementRejects() {
        rejects++;
    }

    public void setRejects(int rejects) {
        this.rejects = rejects;
    }
}

