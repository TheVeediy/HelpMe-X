package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.models.HelpRequest;
import com.qhelpme.utils.Localization;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpListCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpListCommand(HelpRequestManager manager) {
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

        List<HelpRequest> pending = manager.getPendingRequests();
        if (pending.isEmpty()) {
            MessageUtils.sendKey(helper, "helplist.empty");
            return true;
        }

        MessageUtils.sendMessage(helper, Localization.tr("helplist.header", pending.size()));
        for (HelpRequest ticket : pending) {
            Player source = Bukkit.getPlayer(ticket.getPlayerUUID());
            if (source == null) continue;
            MessageUtils.clickableAccept(helper, ticket.getPlayerName(), ticket.getTask(),
                "/helpaccept " + ticket.getPlayerName());
        }
        return true;
    }
}

