package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpRejectCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpRejectCommand(HelpRequestManager manager) {
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

        if (args.length == 0) {
            MessageUtils.sendKey(helper, "helpreject.usage");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            MessageUtils.sendKey(helper, "general.player-offline");
            return true;
        }

        if (!manager.hasActiveRequest(target.getUniqueId())) {
            MessageUtils.sendKey(helper, "helpreject.no-request");
            return true;
        }

        manager.rejectRequest(helper, target.getUniqueId());
        MessageUtils.sendKey(helper, "helpreject.helper-confirm", target.getName());
        MessageUtils.sendKey(target, "helpreject.player-notify", helper.getName());
        return true;
    }
}

