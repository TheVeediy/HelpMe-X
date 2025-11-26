package com.qhelpme.utils;

import java.util.HashMap;
import java.util.Map;

public final class Localization {

    private static Language language = Language.EN;
    private static final Map<String, String> EN = new HashMap<>();
    private static final Map<String, String> FA = new HashMap<>();

    static {
        // General
        EN.put("general.only-players", "&cThis command may only be executed by an in-game player.");
        EN.put("general.no-permission", "&cYou do not have permission to perform this command.");
        EN.put("general.player-offline", "&cThe specified player is not currently online.");

        FA.put("general.only-players", "&cاین دستور فقط توسط بازیکنانی که در سرور حضور دارند قابل اجرا است.");
        FA.put("general.no-permission", "&cاجازه لازم برای اجرای این کامند را ندارید.");
        FA.put("general.player-offline", "&cبازیکن مورد نظر در حال حاضر آنلاین نیست.");

        // /helpme
        EN.put("helpme.usage", "&7Usage: /helpme <explain your issue>");
        EN.put("helpme.details-required", "&cPlease describe your issue with at least three characters.");
        EN.put("helpme.request-exists", "&cYou already have a pending help request.");
        EN.put("helpme.submitted", "&aYour help request has been submitted. A staff member will respond shortly.");
        EN.put("helpme.broadcast", "&e%s &7has submitted a help request: &f%s");

        FA.put("helpme.usage", "&7نحوه استفاده: /helpme <شرح مشکل>");
        FA.put("helpme.details-required", "&cلطفاً مشکل خود را با حداقل سه کاراکتر توضیح دهید.");
        FA.put("helpme.request-exists", "&cشما هم اکنون یک درخواست کمک باز دارید.");
        FA.put("helpme.submitted", "&aدرخواست شما ثبت شد. یکی از استف‌ها به‌زودی پاسخ خواهد داد.");
        FA.put("helpme.broadcast", "&e%s &7درخواست کمک ارسال کرده است: &f%s");

        // /helpaccept
        EN.put("helpaccept.usage", "&7Usage: /helpaccept <player>");
        EN.put("helpaccept.no-request", "&cThat player does not currently have a pending request.");
        EN.put("helpaccept.helper-confirm", "&aYou accepted %s's request. Use /helpchat to communicate.");
        EN.put("helpaccept.player-notify", "&a%s accepted your help request. You may now use /helpchat.");

        FA.put("helpaccept.usage", "&7نحوه استفاده: /helpaccept <بازیکن>");
        FA.put("helpaccept.no-request", "&cاین بازیکن درخواستی برای بررسی ندارد.");
        FA.put("helpaccept.helper-confirm", "&aشما درخواست %s را پذیرفتید. برای گفتگو از /helpchat استفاده کنید.");
        FA.put("helpaccept.player-notify", "&a%s درخواست کمک شما را پذیرفت. اکنون می‌توانید با /helpchat صحبت کنید.");

        // /helplist
        EN.put("helplist.empty", "&7There are no pending requests at the moment.");
        EN.put("helplist.header", "&7Pending requests: &a%d");
        EN.put("helplist.accept-button", "[Accept]");
        EN.put("helplist.accept-hover", "Click to accept %s's request");

        FA.put("helplist.empty", "&7در حال حاضر هیچ درخواستی در لیست نیست.");
        FA.put("helplist.header", "&7تعداد درخواست‌های در انتظار: &a%d");
        FA.put("helplist.accept-button", "[پذیرش]");
        FA.put("helplist.accept-hover", "برای پذیرش درخواست %s کلیک کنید");

        // /helpreject
        EN.put("helpreject.usage", "&7Usage: /helpreject <player>");
        EN.put("helpreject.no-request", "&cThat player does not currently have a pending request.");
        EN.put("helpreject.helper-confirm", "&7You declined %s's request.");
        EN.put("helpreject.player-notify", "&cYour help request was declined by %s.");

        FA.put("helpreject.usage", "&7نحوه استفاده: /helpreject <بازیکن>");
        FA.put("helpreject.no-request", "&cاین بازیکن درخواستی برای بررسی ندارد.");
        FA.put("helpreject.helper-confirm", "&7شما درخواست %s را رد کردید.");
        FA.put("helpreject.player-notify", "&cدرخواست کمک شما توسط %s رد شد.");

        // /helptp
        EN.put("helptp.usage", "&7Usage: /helptp <player>");
        EN.put("helptp.offline", "&cThat player is not online.");
        EN.put("helptp.not-assigned", "&cYou are not assigned to that player. Please accept the request first.");
        EN.put("helptp.teleported", "&aYou have been teleported to %s.");

        FA.put("helptp.usage", "&7نحوه استفاده: /helptp <بازیکن>");
        FA.put("helptp.offline", "&cاین بازیکن آنلاین نیست.");
        FA.put("helptp.not-assigned", "&cشما به این بازیکن اختصاص داده نشده‌اید. ابتدا درخواست را بپذیرید.");
        FA.put("helptp.teleported", "&aشما به %s منتقل شدید.");

        // /helpchat
        EN.put("helpchat.usage", "&7Usage: /helpchat <message>");
        EN.put("helpchat.no-session", "&cYou do not have an active help session.");
        EN.put("helpchat.counterpart-offline", "&cThe other party is offline. The session has been closed.");
        EN.put("helpchat.role-player", "Player");
        EN.put("helpchat.role-helper", "Helper");
        EN.put("helpchat.role-you", "You");

        FA.put("helpchat.usage", "&7نحوه استفاده: /helpchat <پیام>");
        FA.put("helpchat.no-session", "&cشما درخواست کمک فعال ندارید.");
        FA.put("helpchat.counterpart-offline", "&cسمت مقابل آفلاین شد و دریخواست کمک بسته شد.");
        FA.put("helpchat.role-player", "بازیکن");
        FA.put("helpchat.role-helper", "راهنما");
        FA.put("helpchat.role-you", "شما");

        // /helpcomplete
        EN.put("helpcomplete.no-session", "&cYou are not currently assigned to a player.");
        EN.put("helpcomplete.player-offline", "&cThe player you were assisting is no longer online. The session has been closed.");
        EN.put("helpcomplete.helper-confirm", "&aHelp session marked as complete.");
        EN.put("helpcomplete.player-notify", "&a%s has marked your help session as complete.");

        FA.put("helpcomplete.no-session", "&cهیچ درخواست کمک فعالی برای شما ثبت نشده است.");
        FA.put("helpcomplete.player-offline", "&cبازیکنی که در حال کمک به او بودید دیگر آنلاین نیست؛ درخواست کمک بسته شد.");
        FA.put("helpcomplete.helper-confirm", "&aدرخواست کمک به پایان رسید.");
        FA.put("helpcomplete.player-notify", "&a%s درخواست کمک کمک شما را خاتمه داد.");

        // /helpadmins
        EN.put("helpadmins.empty", "&7No helper statistics are available yet.");
        EN.put("helpadmins.header", "&7Helper statistics:");
        EN.put("helpadmins.entry", "&e%s &7- &aAccepts: %d &7/ &cRejects: %d");

        FA.put("helpadmins.empty", "&7هنوز آماری برای راهنماها ثبت نشده است.");
        FA.put("helpadmins.header", "&7آمار راهنماها:");
        FA.put("helpadmins.entry", "&e%s &7- &aقبولی‌ها: %d &7/ &cردی‌ها: %d");
    }

    private Localization() {
    }

    public static void setLanguage(Language lang) {
        language = lang == null ? Language.EN : lang;
    }

    public static Language getLanguage() {
        return language;
    }

    public static String tr(String key, Object... args) {
        String template = (language == Language.FA ? FA : EN).getOrDefault(key, key);
        return args.length == 0 ? template : String.format(template, args);
    }
}


