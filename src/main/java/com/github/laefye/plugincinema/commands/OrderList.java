package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.menus.OrderMenu;
import com.github.laefye.plugincinema.order.interfaces.Listable;
import com.github.laefye.plugincinema.api.LobbyCommandExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class OrderList extends LobbyCommandExecutor {
    @Override
    public boolean onCommand(Player player, Lobby lobby, String[] args) {
        var order = lobby.order;
        if (lobby.getOrderType(Listable.class, player) == null) {
            return false;
        }
        var active = order.active();
        if (active == null) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.order_empty"));
            return true;
        }
        new OrderMenu(new ArrayList<>(((Listable) order).getEntries()), player);
        return true;
    }
}
