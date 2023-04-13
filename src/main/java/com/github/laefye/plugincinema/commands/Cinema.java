package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.menus.CinemaMenu;
import com.github.laefye.plugincinema.permissions.StaticPermissions;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Cinema implements CommandExecutor, TabCompleter {
    private final String version;

    public Cinema(String version) {
        this.version = version;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) { // Нужнео сделать менее костыльно
            var lobby = PluginCinema.getInstance().getLobbyByPlayer((Player) sender);
            if (lobby == null) {
                sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.need_join"));
                return false;
            }
            new CinemaMenu(lobby, (Player) sender);
            return true;
        }
        sender.sendMessage("Version: " + version);
        if (StaticPermissions.hasNotPermission(StaticPermissions.Permission.CINEMA, ((Player) sender))) {
            return false;
        }
        var cmd = PluginCinema.getInstance().register.getCinemaCommand(args[0]);
        cmd.ifPresent(comm -> comm.getExecutor().onCommand(sender, command, label, args));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (StaticPermissions.hasNotPermission(StaticPermissions.Permission.CINEMA, ((Player) sender))) {
            return null;
        }
        if (args.length == 1) {
            var list = new ArrayList<String>();
            for (var method : PluginCinema.getInstance().register.cinemaCommands.keySet()) {
                if (method.startsWith(args[0])) {
                    list.add(method);
                }
            }
            return list;
        }
        if (args.length > 1) {
            var cmd = PluginCinema.getInstance().register.getCinemaCommand(args[0]);
            if (cmd.isPresent() && cmd.get().getTabCompleter() != null) {
                return cmd.get().getTabCompleter().onTabComplete(sender, command, label, args);
            }
        }
        return null;
    }
}
