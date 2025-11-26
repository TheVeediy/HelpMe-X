package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HelpCompleteCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpCompleteCommand(HelpRequestManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player helper)) {
            MessageUtils.sendKey(sender, "general.only-players");
            return true;
        }

        if (!helper.hasPermission("helpme.helper")) {
            MessageUtils.sendKey(helper, "general.no-permission");
            return true;
        }

        UUID playerId = manager.getPlayerForHelper(helper.getUniqueId());
        if (playerId == null) {
            MessageUtils.sendKey(helper, "helpcomplete.no-session");
            return true;
        }

        Player player = Bukkit.getPlayer(playerId);
        if (player == null || !player.isOnline()) {
            MessageUtils.sendKey(helper, "helpcomplete.player-offline");
            manager.endHelperSession(helper.getUniqueId());
            return true;
        }

        manager.endHelperSession(helper.getUniqueId());
        MessageUtils.sendKey(helper, "helpcomplete.helper-confirm");
        MessageUtils.sendKey(player, "helpcomplete.player-notify", helper.getName());
        return true;
    }
}

