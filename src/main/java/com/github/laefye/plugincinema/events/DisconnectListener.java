package com.github.laefye.plugincinema.events;

import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DisconnectListener implements Listener
{
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        var player = event.getPlayer();
        var lobby = PluginCinema.getInstance().getLobbyByPlayer(player);
        if (lobby != null) {
            lobby.exit(player);
        }
    }
}
