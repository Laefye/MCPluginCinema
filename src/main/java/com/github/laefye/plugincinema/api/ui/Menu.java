package com.github.laefye.plugincinema.api.ui;

import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Menu {
    public Inventory inventory;

    public Menu(String title) {
        PluginCinema.getInstance().menus.add(this);
        inventory = Bukkit.createInventory(null, 9 * 6, title);
    }

    public void onClose() {
        PluginCinema.getInstance().menus.remove(this);
    }

    public void onClick(Player player, int slot) {
    }
}
