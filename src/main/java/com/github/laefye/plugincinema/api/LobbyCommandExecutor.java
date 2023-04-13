package com.github.laefye.plugincinema.api;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class LobbyCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var lobby = PluginCinema.getInstance().getLobbyByPlayer((Player) sender);
        if (lobby == null) {
            sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.need_join"));
            return false;
        }
        return onCommand((Player) sender, lobby, args);
    }

    public abstract boolean onCommand(Player player, Lobby lobby, String[] args);
}
