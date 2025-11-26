package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.Localization;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpMeCommand implements CommandExecutor {

    private final HelpRequestManager helpRequestManager;

    public HelpMeCommand(HelpRequestManager helpRequestManager) {
        this.helpRequestManager = helpRequestManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            MessageUtils.sendKey(sender, "general.only-players");
            return true;
        }

        if (!player.hasPermission("helpme.use")) {
            MessageUtils.sendKey(player, "general.no-permission");
            return true;
        }

        if (helpRequestManager.hasActiveRequest(player.getUniqueId())) {
            MessageUtils.sendKey(player, "helpme.request-exists");
            return true;
        }

        if (args.length < 1) {
            MessageUtils.sendKey(player, "helpme.usage");
            return true;
        }

        String task = String.join(" ", args).trim();
        if (task.length() < 3) {
            MessageUtils.sendKey(player, "helpme.details-required");
            return true;
        }

        helpRequestManager.createRequest(player, task);
        MessageUtils.sendKey(player, "helpme.submitted");

        String broadcast = Localization.tr("helpme.broadcast", player.getName(), task);
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> p.hasPermission("helpme.helper"))
            .forEach(p -> MessageUtils.sendMessage(p, broadcast));

        return true;
    }
}

