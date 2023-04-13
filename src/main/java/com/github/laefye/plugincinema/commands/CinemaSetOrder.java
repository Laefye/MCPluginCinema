package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.api.LobbyCommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CinemaSetOrder extends LobbyCommandExecutor implements TabCompleter {
    public static String ID = PluginCinema.id("setOrder");


    @Override
    public boolean onCommand(Player player, Lobby lobby, String[] args) {
        var register = PluginCinema.getInstance().register;
        var order = register.getOrderBuilder(args[1]);
        if (order.isEmpty()) {
            player.sendMessage("Invalid order type");
            return false;
        }
        lobby.order.dispose();
        lobby.order = order.get().apply(lobby);
        player.sendMessage("Order is changed");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        var list = new ArrayList<String>();
        for (var order : PluginCinema.getInstance().register.orderBuilders.keySet()) {
            if (order.startsWith(args[1])) {
                list.add(order);
            }
        }
        return list;
    }
}
