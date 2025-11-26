package com.qhelpme.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HelpRequest {

    private final UUID playerUUID;
    private final String playerName;
    private final Location snapshotLocation;
    private final long timestamp;
    private final String task;
    private UUID helperUUID;
    private String helperName;
    private RequestStatus status;

    public HelpRequest(Player player, String task) {
        this(player.getUniqueId(), player.getName(), player.getLocation().clone(),
            System.currentTimeMillis(), task, null, "", RequestStatus.PENDING);
    }

    public HelpRequest(UUID playerUUID, String playerName, Location snapshotLocation, long timestamp, String task,
                       UUID helperUUID, String helperName, RequestStatus status) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.snapshotLocation = snapshotLocation;
        this.timestamp = timestamp;
        this.task = task;
        this.helperUUID = helperUUID;
        this.helperName = helperName == null ? "" : helperName;
        this.status = status == null ? RequestStatus.PENDING : status;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Location getSnapshotLocation() {
        return snapshotLocation;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTask() {
        return task;
    }

    public UUID getHelperUUID() {
        return helperUUID;
    }

    public void setHelperUUID(UUID helperUUID) {
        this.helperUUID = helperUUID;
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String describe() {
        return playerName + " needs: " + task;
    }

    public enum RequestStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}

