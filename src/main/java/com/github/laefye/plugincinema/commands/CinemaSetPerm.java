package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.permissions.defaults.DefaultPermManager;
import com.github.laefye.plugincinema.api.LobbyCommandExecutor;
import com.github.laefye.videoapi.AbstractService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Stream;

public class CinemaSetPerm extends LobbyCommandExecutor implements TabCompleter {
    public static String ID = PluginCinema.id("setPerm");


    @Override
    public boolean onCommand(Player player, Lobby lobby, String[] args) {
        if (!(lobby.permManager instanceof DefaultPermManager permManager)) {
            player.sendMessage("Invalid perm manager");
            return true;
        }
        if (args.length == 1) {
            player.sendMessage("Invalid arguments");
            return false;
        }
        if (args[1].equals("player")) {
            if (args.length != 4) {
                player.sendMessage("Invalid arguments");
                return false;
            }
            if (args[3].equals("reset")) {
                permManager.resetPlayer(args[2]);
                player.sendMessage("Player permission is reset");
                return true;
            }
            var value = Boolean.parseBoolean(args[3]);
            permManager.addPlayer(args[2], value);
            player.sendMessage("Player permission is changed");
        }
        if (args[1].equals("service")) {
            if (args.length != 4) {
                player.sendMessage("Invalid arguments");
                return false;
            }
            var service = PluginCinema.getInstance().mediaResolver.servicesRouter.getService(args[2]);
            if (service == null) {
                player.sendMessage("Invalid service");
                return false;
            }
            var value = Boolean.parseBoolean(args[3]);
            permManager.setService(service, value);
            player.sendMessage("Service permission is changed");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            return Stream.of("player", "service").filter(s -> s.startsWith(args[1])).toList();
        }
        if (args[1].equals("player")) {
            if (args.length == 3) {
                return null;
            }
            if (args.length == 4) {
                return Stream.of("true", "false", "reset").filter(s -> s.startsWith(args[3])).toList();
            }
        }
        if (args[1].equals("service")) {
            if (args.length == 3) {
                return PluginCinema.getInstance().mediaResolver.servicesRouter.services.stream().map(AbstractService::getName).filter(s -> s.startsWith(args[2])).toList();
            }
            if (args.length == 4) {
                return Stream.of("true", "false").filter(s -> s.startsWith(args[3])).toList();
            }
        }
        return null;
    }
}
