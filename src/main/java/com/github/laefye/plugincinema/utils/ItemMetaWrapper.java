package com.github.laefye.plugincinema.utils;

import com.github.laefye.plugincinema.utils.heads.PlayerProfileTools;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class ItemMetaWrapper {
    private final ItemMeta meta;
    private final ItemStack stack;

    public ItemMetaWrapper(ItemStack stack) {
        this.stack = stack;
        meta = stack.getItemMeta();
    }

    public ItemMetaWrapper setDisplayName(String displayName) {
        if (meta != null) {
            meta.setDisplayName(displayName);
        }
        return this;
    }

    public ItemMetaWrapper setLore(List<String> lore) {
        if (meta != null) {
            meta.setLore(lore);
        }
        return this;
    }

    public ItemMetaWrapper setHead(String texture) {
        if (meta != null && meta instanceof SkullMeta skull) {
            var profile = PlayerProfileTools.getProfile("https://textures.minecraft.net/texture/" + texture);
            skull.setOwnerProfile(profile);
        }
        return this;
    }

    public ItemStack apply() {
        stack.setItemMeta(meta);
        return stack;
    }
}
