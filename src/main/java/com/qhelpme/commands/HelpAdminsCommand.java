package com.qhelpme.commands;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.models.HelperStats;
import com.qhelpme.utils.Localization;
import com.qhelpme.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HelpAdminsCommand implements CommandExecutor {

    private final HelpRequestManager manager;

    public HelpAdminsCommand(HelpRequestManager manager) {
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

        if (manager.getHelperStats().isEmpty()) {
            MessageUtils.sendKey(helper, "helpadmins.empty");
            return true;
        }

        List<HelperStats> sorted = manager.getHelperStats().values().stream()
            .sorted(Comparator.comparingInt(HelperStats::getAccepts).reversed())
            .collect(Collectors.toList());

        MessageUtils.sendMessage(helper, Localization.tr("helpadmins.header"));
        for (HelperStats stat : sorted) {
            MessageUtils.sendMessage(helper,
                Localization.tr("helpadmins.entry", stat.getHelperName(), stat.getAccepts(), stat.getRejects()));
        }
        return true;
    }
}

