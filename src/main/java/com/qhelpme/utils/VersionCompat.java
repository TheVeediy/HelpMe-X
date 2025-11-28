package com.qhelpme.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Method;

public class VersionCompat {

    private static Method setOwningPlayerMethod;
    private static Method setOwnerMethod;
    private static Material skullMaterial;
    private static boolean initialized = false;

    static {
        initialize();
    }

    private static void initialize() {
        if (initialized) return;
        
        try {
            try {
                setOwningPlayerMethod = SkullMeta.class.getMethod("setOwningPlayer", Player.class);
            } catch (NoSuchMethodException e) {
            }
            
            try {
                setOwnerMethod = SkullMeta.class.getMethod("setOwner", String.class);
            } catch (NoSuchMethodException e) {
            }
            
            try {
                skullMaterial = Material.valueOf("PLAYER_HEAD");
            } catch (IllegalArgumentException e) {
                try {
                    skullMaterial = Material.valueOf("SKULL_ITEM");
                } catch (IllegalArgumentException ex) {
                    skullMaterial = null;
                }
            }
        } catch (Exception e) {
        }
        
        initialized = true;
    }

    public static Material getSkullMaterial() {
        if (skullMaterial != null) {
            return skullMaterial;
        }
        try {
            return Material.valueOf("PLAYER_HEAD");
        } catch (IllegalArgumentException e) {
            return Material.valueOf("SKULL_ITEM");
        }
    }

    public static ItemStack createPlayerHead(Player player) {
        if (player == null) {
            return null;
        }
        
        Material material = getSkullMaterial();
        if (material == null) {
            return null;
        }
        
        ItemStack item;
        if (material == Material.SKULL_ITEM) {
            item = new ItemStack(material, 1, (short) 3);
        } else {
            item = new ItemStack(material);
        }
        
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            setSkullOwner(meta, player);
            item.setItemMeta(meta);
        }
        
        return item;
    }

    public static boolean setSkullOwner(SkullMeta meta, Player player) {
        if (meta == null || player == null) {
            return false;
        }

        if (setOwningPlayerMethod != null) {
            try {
                setOwningPlayerMethod.invoke(meta, player);
                return true;
            } catch (Exception e) {
            }
        }

        if (setOwnerMethod != null) {
            try {
                setOwnerMethod.invoke(meta, player.getName());
                return true;
            } catch (Exception e) {
            }
        }

        return false;
    }
}

