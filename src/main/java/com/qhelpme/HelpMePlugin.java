package com.qhelpme;

import com.qhelpme.commands.*;
import com.qhelpme.listeners.HelpListMenuListener;
import com.qhelpme.managers.DataManager;
import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.Language;
import com.qhelpme.utils.Localization;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class HelpMePlugin extends JavaPlugin {

    private static HelpMePlugin instance;
    private HelpRequestManager helpRequestManager;
    private DataManager dataManager;
    private HelpListMenuListener menuListener;
    private final List<String> bootNotes = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        bootNotes.clear();
        bootNotes.add("warming up storage");
        bootNotes.add("wiring commands");

        saveDefaultConfig();
        Language configuredLanguage = Language.fromConfig(getConfig().getString("language"));
        Localization.setLanguage(configuredLanguage);

        dataManager = new DataManager(this);
        helpRequestManager = new HelpRequestManager(dataManager);
        helpRequestManager.loadData();

        menuListener = new HelpListMenuListener(helpRequestManager);
        getServer().getPluginManager().registerEvents(menuListener, this);

        wire("helpme", new HelpMeCommand(helpRequestManager));
        wire("helpaccept", new HelpAcceptCommand(helpRequestManager));
        wire("helplist", new HelpListCommand(helpRequestManager, menuListener));
        wire("helpreject", new HelpRejectCommand(helpRequestManager));
        wire("helptp", new HelpTpCommand(helpRequestManager));
        wire("helpadmins", new HelpAdminsCommand(helpRequestManager));
        wire("helpchat", new HelpChatCommand(helpRequestManager));
        wire("helpcomplete", new HelpCompleteCommand(helpRequestManager));

        long everyFiveMinish = 20L * 60L * 5L;
        getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (dataManager != null && helpRequestManager != null) {
                dataManager.saveAll(helpRequestManager);
            }
        }, everyFiveMinish, everyFiveMinish);

        getLogger().info("HelpMe fired up. Notes: " + bootNotes);
    }

    @Override
    public void onDisable() {
        if (dataManager != null && helpRequestManager != null) {
            dataManager.saveAll(helpRequestManager);
            helpRequestManager.clearAllRequests();
        }
        getLogger().info("HelpMe shut down clean-ish.");
    }

    private void wire(String command, CommandExecutor executor) {
        org.bukkit.command.PluginCommand cmd = getCommand(command);
        if (cmd == null) {
            getLogger().warning("Command " + command + " is missing from plugin.yml (check spelling).");
            return;
        }
        cmd.setExecutor(executor);
    }

    public static HelpMePlugin getInstance() {
        return instance;
    }

    public HelpRequestManager getHelpRequestManager() {
        return helpRequestManager;
    }

    public static String getPrefix() {
        return "&7[&a&lHelpMe&7]";
    }
}

