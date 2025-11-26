package com.qhelpme.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class MessageUtils {

    private static final String PREFIX = "&7[&a&lHelpMe&7]";

    private MessageUtils() {
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(color(PREFIX + " " + text));
    }

    public static void sendKey(CommandSender sender, String key, Object... args) {
        sendMessage(sender, Localization.tr(key, args));
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void clickableAccept(Player receiver, String playerName, String task, String command) {
        Component base = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("HelpMe", NamedTextColor.GREEN, TextDecoration.BOLD))
            .append(Component.text("] ", NamedTextColor.GRAY));

        Component msg = base
            .append(Component.text(playerName, NamedTextColor.YELLOW))
            .append(Component.text(": " + task + " ", NamedTextColor.WHITE))
            .append(Component.text(Localization.tr("helplist.accept-button"), NamedTextColor.GREEN, TextDecoration.BOLD)
                .clickEvent(ClickEvent.runCommand(command))
                .hoverEvent(HoverEvent.showText(Component.text(
                    Localization.tr("helplist.accept-hover", playerName), NamedTextColor.GRAY))));

        receiver.sendMessage(msg);
    }

    public static void relayHelpChat(Player recipient, String role, String name, String message) {
        Component prefix = Component.text("[", NamedTextColor.GRAY)
            .append(Component.text("HelpMe", NamedTextColor.GREEN, TextDecoration.BOLD))
            .append(Component.text("] ", NamedTextColor.GRAY));

        Component full = prefix
            .append(Component.text(role, resolveRoleColor(role)))
            .append(Component.text(" ", NamedTextColor.DARK_GRAY))
            .append(Component.text(name, NamedTextColor.WHITE))
            .append(Component.text(": ", NamedTextColor.GRAY))
            .append(Component.text(message, NamedTextColor.WHITE));

        recipient.sendMessage(full);
    }

    private static NamedTextColor resolveRoleColor(String role) {
        String helper = Localization.tr("helpchat.role-helper");
        String player = Localization.tr("helpchat.role-player");
        String you = Localization.tr("helpchat.role-you");

        if (helper.equalsIgnoreCase(role)) return NamedTextColor.AQUA;
        if (player.equalsIgnoreCase(role)) return NamedTextColor.YELLOW;
        if (you.equalsIgnoreCase(role)) return NamedTextColor.GOLD;
        return NamedTextColor.GREEN;
    }
}

