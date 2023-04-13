package com.github.laefye.plugincinema.events;

import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryEvents implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        var menu = PluginCinema.getInstance().getMenuByInventory(event.getInventory());
        if (menu == null) {
            return;
        }
        menu.onClick((Player) event.getWhoClicked(), event.getSlot());
        event.setCancelled(true);
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        var menu = PluginCinema.getInstance().getMenuByInventory(event.getInventory());
        if (menu == null) {
            return;
        }
        menu.onClose();
    }
}
