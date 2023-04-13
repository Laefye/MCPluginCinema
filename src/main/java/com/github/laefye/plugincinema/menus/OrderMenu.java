package com.github.laefye.plugincinema.menus;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.order.OrderEntry;
import com.github.laefye.plugincinema.api.ui.MenuWithPages;
import com.github.laefye.plugincinema.utils.ItemMetaWrapper;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OrderMenu extends MenuWithPages<OrderEntry> {
    public OrderMenu(ArrayList<OrderEntry> elements, Player player) {
        super(elements, PluginCinema.getInstance().lang.getPhrase("video.order.menu"));
        player.openInventory(inventory);
    }

    @Override
    protected ItemStack setItem(int slot, OrderEntry element) {
        return new ItemMetaWrapper(new ItemStack(Material.PLAYER_HEAD))
                .setHead(PluginCinema.getInstance().mediaResolver.servicesRouter.getServiceOrCustom(element.publicUrl).getIconTexture())
                .setDisplayName(element.videoInfo.title)
                .setLore(List.of(
                        PluginCinema.getInstance().lang.getPhraseFormatted("video.order.who_added", element.username)
                ))
                .apply();
    }

    @Override
    public void onClick(Player player, int slot, OrderEntry element) {
        player.closeInventory();
        player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsFormattedBuilder("video.order.link", element.publicUrl)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, element.publicUrl.toString()))
                .create());
        if (element.isActive() && element.videoInfo.duration > 0) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsFormattedComponent("video.order.timecode", element.getTimecode().toString()));
        }
    }
}
