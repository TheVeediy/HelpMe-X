package com.qhelpme.listeners;

import com.qhelpme.managers.HelpRequestManager;
import com.qhelpme.models.HelpRequest;
import com.qhelpme.utils.Localization;
import com.qhelpme.utils.MessageUtils;
import com.qhelpme.utils.VersionCompat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class HelpListMenuListener implements Listener {

    private final HelpRequestManager manager;
    private final Map<UUID, Map<Integer, UUID>> openMenus = new HashMap<>();

    public HelpListMenuListener(HelpRequestManager manager) {
        this.manager = manager;
    }

    public void openMenu(Player helper, List<HelpRequest> requests) {
        int size = (int) Math.ceil(requests.size() / 9.0) * 9;
        if (size < 45) size = 45;
        if (size > 54) size = 54;

        org.bukkit.inventory.Inventory menu = Bukkit.createInventory(null, size, 
            MessageUtils.color(Localization.tr("helplist.menu-title")));

        Map<Integer, UUID> slotToPlayer = new HashMap<>();
        for (int i = 0; i < requests.size() && i < 54; i++) {
            HelpRequest request = requests.get(i);
            Player target = Bukkit.getPlayer(request.getPlayerUUID());
            if (target == null) continue;

            ItemStack item = VersionCompat.createPlayerHead(target);
            if (item == null) {
                item = new ItemStack(VersionCompat.getSkullMaterial());
            }
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta != null) {
                VersionCompat.setSkullOwner(meta, target);
                meta.setDisplayName(MessageUtils.color("&e" + request.getPlayerName()));
                
                List<String> lore = new ArrayList<>();
                lore.add(MessageUtils.color(Localization.tr("helplist.menu-request-label") + " &f" + request.getTask()));
                lore.add("");
                lore.add(MessageUtils.color(Localization.tr("helplist.menu-left-click")));
                lore.add(MessageUtils.color(Localization.tr("helplist.menu-right-click")));
                
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            menu.setItem(i, item);
            slotToPlayer.put(i, request.getPlayerUUID());
        }

        openMenus.put(helper.getUniqueId(), slotToPlayer);
        helper.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player helper = (Player) event.getWhoClicked();

        Map<Integer, UUID> slotMap = openMenus.get(helper.getUniqueId());
        if (slotMap == null) {
            return;
        }

        event.setCancelled(true);

        if (!helper.hasPermission("helpme.helper")) {
            return;
        }

        int slot = event.getSlot();
        UUID playerUUID = slotMap.get(slot);
        if (playerUUID == null) {
            return;
        }

        Player target = Bukkit.getPlayer(playerUUID);
        if (target == null) {
            MessageUtils.sendKey(helper, "general.player-offline");
            helper.closeInventory();
            openMenus.remove(helper.getUniqueId());
            return;
        }

        if (!manager.hasActiveRequest(playerUUID)) {
            MessageUtils.sendKey(helper, "helpaccept.no-request");
            helper.closeInventory();
            openMenus.remove(helper.getUniqueId());
            return;
        }

        if (event.isLeftClick()) {
            manager.acceptRequest(helper, playerUUID);
            MessageUtils.sendKey(helper, "helpaccept.helper-confirm", target.getName());
            MessageUtils.sendKey(target, "helpaccept.player-notify", helper.getName());
            helper.closeInventory();
            openMenus.remove(helper.getUniqueId());
        } else if (event.isRightClick()) {
            manager.rejectRequest(helper, playerUUID);
            MessageUtils.sendKey(helper, "helpreject.helper-confirm", target.getName());
            MessageUtils.sendKey(target, "helpreject.player-notify", helper.getName());
            helper.closeInventory();
            openMenus.remove(helper.getUniqueId());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            openMenus.remove(player.getUniqueId());
        }
    }

    public void cleanup(Player player) {
        openMenus.remove(player.getUniqueId());
    }
}

