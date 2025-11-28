package com.qhelpme.managers;

import com.qhelpme.HelpMePlugin;
import com.qhelpme.models.HelpRequest;
import com.qhelpme.models.HelperStats;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager {

    private final HelpMePlugin plugin;
    private FileConfiguration dataConfig;
    private File dataFile;

    public DataManager(HelpMePlugin plugin) {
        this.plugin = plugin;
        prepareFile();
    }

    private void prepareFile() {
        File dataDir = plugin.getDataFolder();
        if (!dataDir.exists() && !dataDir.mkdirs()) {
            plugin.getLogger().warning("Could not create plugin data folder, weird.");
        }

        dataFile = new File(dataDir, "data.yml");
        if (!dataFile.exists()) {
            try {
                if (dataFile.createNewFile()) {
                    plugin.getLogger().info("Dropped new data.yml");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to touch data.yml");
            }
        }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    public void saveHelperStats(Map<UUID, HelperStats> helperStats) {
        dataConfig.set("helper-stats", null);
        for (HelperStats stats : helperStats.values()) {
            String base = "helper-stats." + stats.getHelperUUID();
            dataConfig.set(base + ".name", stats.getHelperName());
            dataConfig.set(base + ".accepts", stats.getAccepts());
            dataConfig.set(base + ".rejects", stats.getRejects());
        }
        push();
    }

    public Map<UUID, HelperStats> loadHelperStats() {
        Map<UUID, HelperStats> stats = new ConcurrentHashMap<>();
        org.bukkit.configuration.ConfigurationSection section = dataConfig.getConfigurationSection("helper-stats");
        if (section == null) return stats;

        for (String key : section.getKeys(false)) {
            try {
                UUID id = UUID.fromString(key);
                String name = section.getString(key + ".name", "helper-" + key.substring(0, 6));
                int accepts = section.getInt(key + ".accepts", 0);
                int rejects = section.getInt(key + ".rejects", 0);

                HelperStats helperStats = new HelperStats(id, name);
                helperStats.setAccepts(accepts);
                helperStats.setRejects(rejects);
                stats.put(id, helperStats);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Bad helper uuid '" + key + "'");
            }
        }
        return stats;
    }

    public void saveActiveSessions(Map<UUID, UUID> playerToHelper, Map<UUID, UUID> helperToPlayer) {
        dataConfig.set("active-sessions", null);
        for (Map.Entry<UUID, UUID> entry : playerToHelper.entrySet()) {
            String base = "active-sessions." + entry.getKey();
            dataConfig.set(base + ".player-uuid", entry.getKey().toString());
            dataConfig.set(base + ".helper-uuid", entry.getValue().toString());
        }
        push();
    }

    public void loadActiveSessions(Map<UUID, UUID> playerToHelper, Map<UUID, UUID> helperToPlayer) {
        org.bukkit.configuration.ConfigurationSection section = dataConfig.getConfigurationSection("active-sessions");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                UUID playerId = UUID.fromString(section.getString(key + ".player-uuid"));
                UUID helperId = UUID.fromString(section.getString(key + ".helper-uuid"));
                playerToHelper.put(playerId, helperId);
                helperToPlayer.put(helperId, playerId);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Bad session entry '" + key + "'");
            }
        }
    }

    public void savePendingRequests(Collection<HelpRequest> requests) {
        dataConfig.set("pending-requests", null);
        for (HelpRequest request : requests) {
            String base = "pending-requests." + request.getPlayerUUID();
            dataConfig.set(base + ".player-name", request.getPlayerName());
            dataConfig.set(base + ".task", request.getTask());
            dataConfig.set(base + ".timestamp", request.getTimestamp());
            dataConfig.set(base + ".status", request.getStatus().name());

            Location loc = request.getSnapshotLocation();
            if (loc != null && loc.getWorld() != null) {
                dataConfig.set(base + ".location.world", loc.getWorld().getName());
                dataConfig.set(base + ".location.x", loc.getX());
                dataConfig.set(base + ".location.y", loc.getY());
                dataConfig.set(base + ".location.z", loc.getZ());
                dataConfig.set(base + ".location.yaw", loc.getYaw());
                dataConfig.set(base + ".location.pitch", loc.getPitch());
            }
        }
        push();
    }

    public void loadPendingRequests(Map<UUID, HelpRequest> requests) {
        org.bukkit.configuration.ConfigurationSection section = dataConfig.getConfigurationSection("pending-requests");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            try {
                UUID playerUUID = UUID.fromString(key);
                String name = section.getString(key + ".player-name", "Unknown");
                String task = section.getString(key + ".task", "");
                long timestamp = section.getLong(key + ".timestamp", System.currentTimeMillis());
                String statusName = section.getString(key + ".status", HelpRequest.RequestStatus.PENDING.name());
                HelpRequest.RequestStatus status;
                try {
                    status = HelpRequest.RequestStatus.valueOf(statusName);
                } catch (IllegalArgumentException ex) {
                    status = HelpRequest.RequestStatus.PENDING;
                }

                Location location = null;
                String worldName = section.getString(key + ".location.world");
                if (worldName != null) {
                    World world = plugin.getServer().getWorld(worldName);
                    if (world != null) {
                        double x = section.getDouble(key + ".location.x");
                        double y = section.getDouble(key + ".location.y");
                        double z = section.getDouble(key + ".location.z");
                        float yaw = (float) section.getDouble(key + ".location.yaw");
                        float pitch = (float) section.getDouble(key + ".location.pitch");
                        location = new Location(world, x, y, z, yaw, pitch);
                    }
                }

                HelpRequest request = new HelpRequest(playerUUID, name, location, timestamp, task, null, "", status);
                if (status == HelpRequest.RequestStatus.PENDING) {
                    requests.put(playerUUID, request);
                }
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("Bad pending request entry '" + key + "'");
            }
        }
    }

    private void push() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Couldn't save data.yml: " + e.getMessage());
        }
    }

    public void saveAll(HelpRequestManager manager) {
        saveHelperStats(manager.getHelperStats());
        saveActiveSessions(manager.getActiveHelpSessions(), manager.getHelperToPlayerSessions());
        savePendingRequests(manager.getAllRequests());
    }
}

