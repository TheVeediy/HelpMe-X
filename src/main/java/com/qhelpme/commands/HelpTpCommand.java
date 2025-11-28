package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HelpTpCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpTpCommand(HelpRequestManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendKey(sender, "general.only-players");
            return true;
        }
        Player helper = (Player) sender;

        if (!helper.hasPermission("helpme.helper")) {
            MessageUtils.sendKey(helper, "general.no-permission");
            return true;
        }

        if (args.length == 0) {
            MessageUtils.sendKey(helper, "helptp.usage");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            MessageUtils.sendKey(helper, "helptp.offline");
            return true;
        }

        UUID attachedPlayer = manager.getPlayerForHelper(helper.getUniqueId());
        if (attachedPlayer == null || !attachedPlayer.equals(target.getUniqueId())) {
            MessageUtils.sendKey(helper, "helptp.not-assigned");
            return true;
        }

        helper.teleport(target.getLocation());
        MessageUtils.sendKey(helper, "helptp.teleported", target.getName());
        return true;
    }
}

