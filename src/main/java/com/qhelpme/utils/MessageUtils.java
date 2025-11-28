package com.qhelpme.utils;

import com.qhelpme.HelpMePlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public final class MessageUtils {

    private static final String PREFIX = "&7[&a&lHelpMe&7]";
    private static Method spigotSendMessageMethod;
    private static boolean reflectionInitialized = false;
    private static final Pattern COMMAND_PATTERN = Pattern.compile("(/[a-zA-Z0-9_]+(?:\\s+[^\\s]+)*)");

    static {
        initializeReflection();
    }

    private static void initializeReflection() {
        if (reflectionInitialized) return;
        
        try {
            try {
                Class<?> spigotClass = Class.forName("org.bukkit.entity.Player$Spigot");
                spigotSendMessageMethod = spigotClass.getMethod("sendMessage", String.class);
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        
        reflectionInitialized = true;
    }

    private MessageUtils() {
    }

    private static boolean isColoredMessagesEnabled() {
        HelpMePlugin plugin = HelpMePlugin.getInstance();
        if (plugin == null) return true;
        return plugin.getConfig().getBoolean("colored-messages", true);
    }

    private static String formatColoredMessage(String text) {
        if (!isColoredMessagesEnabled()) {
            return text;
        }
        
        java.util.regex.Matcher matcher = COMMAND_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        int lastEnd = 0;
        
        while (matcher.find()) {
            sb.append(text.substring(lastEnd, matcher.start()));
            String command = matcher.group(1);
            String lastColor = extractLastColorCode(text.substring(0, matcher.start()));
            sb.append("&e").append(command).append(lastColor);
            lastEnd = matcher.end();
        }
        sb.append(text.substring(lastEnd));
        
        return sb.toString();
    }
    
    private static String extractLastColorCode(String text) {
        int lastColorIndex = text.lastIndexOf('&');
        if (lastColorIndex >= 0 && lastColorIndex < text.length() - 1) {
            char colorChar = text.charAt(lastColorIndex + 1);
            if ((colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || 
                (colorChar >= 'A' && colorChar <= 'F') || colorChar == 'r' || colorChar == 'R') {
                return "&" + colorChar;
            }
        }
        return "&7";
    }

    public static void sendMessage(CommandSender sender, String text) {
        String formattedText = formatColoredMessage(text);
        sender.sendMessage(color(PREFIX + " " + formattedText));
    }

    public static void sendKey(CommandSender sender, String key, Object... args) {
        sendMessage(sender, Localization.tr(key, args));
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void clickableAccept(Player receiver, String playerName, String task, String command) {
        String acceptButton = Localization.tr("helplist.accept-button");
        String hoverText = Localization.tr("helplist.accept-hover", playerName);
        
        String formattedTask = isColoredMessagesEnabled() ? formatColoredMessage(task) : task;
        formattedTask = escapeJson(formattedTask);
        
        String json = "{\"text\":\"\",\"extra\":[" +
            "{\"text\":\"[\",\"color\":\"gray\"}," +
            "{\"text\":\"HelpMe\",\"color\":\"green\",\"bold\":true}," +
            "{\"text\":\"] \",\"color\":\"gray\"}," +
            "{\"text\":\"" + escapeJson(playerName) + "\",\"color\":\"yellow\"}," +
            "{\"text\":\": " + formattedTask + " \"}," +
            "{\"text\":\"" + escapeJson(acceptButton) + "\"," +
            "\"color\":\"green\"," +
            "\"bold\":true," +
            "\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + escapeJson(command) + "\"}," +
            "\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"" + escapeJson(hoverText) + "\",\"color\":\"gray\"}}" +
            "}" +
            "]}";
        
        sendJsonMessage(receiver, json);
    }

    public static void relayHelpChat(Player recipient, String role, String name, String message) {
        String roleColor = getRoleColorCode(role);
        
        String formattedMessage = isColoredMessagesEnabled() ? formatColoredMessage(message) : message;
        formattedMessage = escapeJson(formattedMessage);
        
        String json = "{\"text\":\"\",\"extra\":[" +
            "{\"text\":\"[\",\"color\":\"gray\"}," +
            "{\"text\":\"HelpMe\",\"color\":\"green\",\"bold\":true}," +
            "{\"text\":\"] \",\"color\":\"gray\"}," +
            "{\"text\":\"" + escapeJson(role) + "\",\"color\":\"" + roleColor + "\"}," +
            "{\"text\":\" \",\"color\":\"dark_gray\"}," +
            "{\"text\":\"" + escapeJson(name) + "\",\"color\":\"white\"}," +
            "{\"text\":\": \",\"color\":\"gray\"}," +
            "{\"text\":\"" + formattedMessage + "\"}" +
            "]}";
        
        sendJsonMessage(recipient, json);
    }

    private static String getRoleColorCode(String role) {
        String helper = Localization.tr("helpchat.role-helper");
        String player = Localization.tr("helpchat.role-player");
        String you = Localization.tr("helpchat.role-you");

        if (helper.equalsIgnoreCase(role)) return "aqua";
        if (player.equalsIgnoreCase(role)) return "yellow";
        if (you.equalsIgnoreCase(role)) return "gold";
        return "green";
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }

    private static void sendJsonMessage(Player player, String json) {
        if (player == null) return;
        
        if (spigotSendMessageMethod != null) {
            try {
                Object spigot = player.getClass().getMethod("spigot").invoke(player);
                spigotSendMessageMethod.invoke(spigot, json);
                return;
            } catch (Exception e) {
            }
        }
        
        String plainText = extractTextFromJson(json);
        player.sendMessage(color(plainText));
    }
    
    private static String extractTextFromJson(String json) {
        StringBuilder result = new StringBuilder();
        int start = 0;
        while (true) {
            int textIndex = json.indexOf("\"text\":\"", start);
            if (textIndex == -1) break;
            
            int textStart = textIndex + 8;
            int textEnd = json.indexOf("\"", textStart);
            if (textEnd == -1) break;
            
            String text = json.substring(textStart, textEnd);
            text = text.replace("\\\"", "\"")
                      .replace("\\n", "\n")
                      .replace("\\r", "\r")
                      .replace("\\t", "\t")
                      .replace("\\\\", "\\");
            result.append(text);
            
            start = textEnd + 1;
        }
        return result.toString().trim();
    }
}

