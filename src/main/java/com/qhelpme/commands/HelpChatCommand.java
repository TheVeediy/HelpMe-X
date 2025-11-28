package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.utils.Localization;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HelpChatCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpChatCommand(HelpRequestManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.sendKey(sender, "general.only-players");
            return true;
        }
        Player talker = (Player) sender;

        if (args.length == 0) {
            MessageUtils.sendKey(talker, "helpchat.usage");
            return true;
        }

        UUID helperId = manager.getHelperForPlayer(talker.getUniqueId());
        UUID playerId = manager.getPlayerForHelper(talker.getUniqueId());

        Player recipient;
        String role;
        boolean senderIsPlayer;

        if (helperId != null) {
            recipient = Bukkit.getPlayer(helperId);
            role = Localization.tr("helpchat.role-player");
            senderIsPlayer = true;
        } else if (playerId != null) {
            recipient = Bukkit.getPlayer(playerId);
            role = Localization.tr("helpchat.role-helper");
            senderIsPlayer = false;
        } else {
            MessageUtils.sendKey(talker, "helpchat.no-session");
            return true;
        }

        if (recipient == null || !recipient.isOnline()) {
            MessageUtils.sendKey(talker, "helpchat.counterpart-offline");
            if (senderIsPlayer) {
                manager.endHelpSession(talker.getUniqueId());
            } else {
                manager.endHelperSession(talker.getUniqueId());
            }
            return true;
        }

        String text = String.join(" ", args);
        MessageUtils.relayHelpChat(recipient, role, talker.getName(), text);
        MessageUtils.relayHelpChat(talker, Localization.tr("helpchat.role-you"), talker.getName(), text);
        return true;
    }
}

