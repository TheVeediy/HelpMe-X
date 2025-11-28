package com.qhelpme.commands;

import com.qhelpme.HelpMePlugin;
import com.qhelpme.listeners.HelpListMenuListener;
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
    private final HelpListMenuListener menuListener;

    public HelpListCommand(HelpRequestManager manager, HelpListMenuListener menuListener) {
        this.manager = manager;
        this.menuListener = menuListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendKey(sender, "general.only-players");
            return true;
        }
        Player helper = (Player) sender;

        if (!helper.hasPermission("helpme.helper")) {
            MessageUtils.sendKey(helper, "general.no-permission");
            return true;
        }

        List<HelpRequest> pending = manager.getPendingRequests();
        if (pending.isEmpty()) {
            MessageUtils.sendKey(helper, "helplist.empty");
            return true;
        }

        String menuMode = HelpMePlugin.getInstance().getConfig().getString("menu-mode", "text");
        if ("menu".equalsIgnoreCase(menuMode)) {
            menuListener.openMenu(helper, pending);
        } else {
            MessageUtils.sendMessage(helper, Localization.tr("helplist.header", pending.size()));
            for (HelpRequest ticket : pending) {
                Player source = Bukkit.getPlayer(ticket.getPlayerUUID());
                if (source == null) continue;
                MessageUtils.clickableAccept(helper, ticket.getPlayerName(), ticket.getTask(),
                    "/helpaccept " + ticket.getPlayerName());
            }
        }
        return true;
    }
}

