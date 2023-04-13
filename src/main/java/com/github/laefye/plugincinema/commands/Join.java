package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.api.EventType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Join implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var player = (Player) sender;
        if (args.length != 1) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.invalid_lobby_name"));
            return false;
        }
        var lobby = PluginCinema.getInstance().lobbies.get(args[0]);
        if (lobby == null) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.invalid_lobby_name"));
            return false;
        }
        if (!lobby.permManager.join(player)) {
            return false;
        }
        PluginCinema.getInstance().lobbies.get(args[0]).join(player);

        player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.status.join"));

        PluginCinema.getInstance().register.executeEvent(EventType.JOIN, player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            ArrayList<String> lobbies = new ArrayList<String>();

            for (var lobby : PluginCinema.getInstance().lobbies.keySet()) {
                if (lobby.startsWith(args[0])) {
                    lobbies.add(lobby);
                }
            }

            Collections.sort(lobbies);
            return lobbies;
        }
        return null;
    }
}
