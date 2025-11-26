package com.qhelpme.managers;

import com.qhelpme.HelpMePlugin;
import com.qhelpme.models.HelpRequest;
import com.qhelpme.models.HelperStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HelpRequestManager {

    private final Map<UUID, HelpRequest> inbox = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> playerToHelper = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> helperToPlayer = new ConcurrentHashMap<>();
    private final Map<UUID, HelperStats> helperStats = new ConcurrentHashMap<>();
    private final List<String> debugNotes = new ArrayList<>();
    private final DataManager dataManager;

    public HelpRequestManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadData() {
        if (dataManager == null) return;
        helperStats.putAll(dataManager.loadHelperStats());
        dataManager.loadPendingRequests(inbox);
        dataManager.loadActiveSessions(playerToHelper, helperToPlayer);
        debugNotes.add("stats=" + helperStats.size());
        debugNotes.add("pending=" + inbox.size());
        debugNotes.add("sessions=" + playerToHelper.size());
    }

    public void createRequest(Player player, String task) {
        HelpRequest request = new HelpRequest(player, task);
        inbox.put(player.getUniqueId(), request);
        saveDataAsync();
    }

    public boolean hasActiveRequest(UUID playerUUID) {
        return inbox.containsKey(playerUUID);
    }

    public HelpRequest getRequest(UUID playerUUID) {
        return inbox.get(playerUUID);
    }

    public Collection<HelpRequest> getAllRequests() {
        return inbox.values();
    }

    public List<HelpRequest> getPendingRequests() {
        List<HelpRequest> pending = new ArrayList<>();
        for (HelpRequest ticket : inbox.values()) {
            if (ticket.getStatus() == HelpRequest.RequestStatus.PENDING) {
                pending.add(ticket);
            }
        }
        pending.sort(Comparator.comparingLong(HelpRequest::getTimestamp));
        return pending;
    }

    public void acceptRequest(Player helper, UUID playerUUID) {
        HelpRequest request = inbox.remove(playerUUID);
        if (request == null) return;
        request.setHelperUUID(helper.getUniqueId());
        request.setHelperName(helper.getName());
        request.setStatus(HelpRequest.RequestStatus.ACCEPTED);

        HelperStats stats = helperStats.computeIfAbsent(helper.getUniqueId(),
            id -> new HelperStats(id, helper.getName()));
        stats.incrementAccepts();

        playerToHelper.put(playerUUID, helper.getUniqueId());
        helperToPlayer.put(helper.getUniqueId(), playerUUID);
        saveDataAsync();
    }

    public void rejectRequest(Player helper, UUID playerUUID) {
        HelpRequest request = inbox.remove(playerUUID);
        if (request == null) return;
        request.setHelperUUID(helper.getUniqueId());
        request.setHelperName(helper.getName());
        request.setStatus(HelpRequest.RequestStatus.REJECTED);

        HelperStats stats = helperStats.computeIfAbsent(helper.getUniqueId(),
            id -> new HelperStats(id, helper.getName()));
        stats.incrementRejects();
        saveDataAsync();
    }

    public void removeRequest(UUID playerUUID) {
        if (inbox.remove(playerUUID) != null) {
            saveDataAsync();
        }
    }

    public void clearAllRequests() {
        inbox.clear();
        playerToHelper.clear();
        helperToPlayer.clear();
    }

    public UUID getHelperForPlayer(UUID playerUUID) {
        return playerToHelper.get(playerUUID);
    }

    public UUID getPlayerForHelper(UUID helperUUID) {
        return helperToPlayer.get(helperUUID);
    }

    public boolean helperHasActiveSession(UUID helperUUID) {
        return helperToPlayer.containsKey(helperUUID);
    }

    public void endHelpSession(UUID playerUUID) {
        UUID helperUUID = playerToHelper.remove(playerUUID);
        if (helperUUID != null) {
            helperToPlayer.remove(helperUUID);
            saveDataAsync();
        }
    }

    public void endHelperSession(UUID helperUUID) {
        UUID playerUUID = helperToPlayer.remove(helperUUID);
        if (playerUUID != null) {
            playerToHelper.remove(playerUUID);
            saveDataAsync();
        }
    }

    public Map<UUID, HelperStats> getHelperStats() {
        return helperStats;
    }

    public Map<UUID, UUID> getActiveHelpSessions() {
        return playerToHelper;
    }

    public Map<UUID, UUID> getHelperToPlayerSessions() {
        return helperToPlayer;
    }

    private void saveDataAsync() {
        if (dataManager == null) return;
        Bukkit.getScheduler().runTaskAsynchronously(HelpMePlugin.getInstance(), () -> dataManager.saveAll(this));
    }
}


