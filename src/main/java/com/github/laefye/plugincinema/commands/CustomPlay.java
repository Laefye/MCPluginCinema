package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.api.LobbyCommandExecutor;
import com.github.laefye.plugincinema.lobby.Lobby;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CustomPlay extends LobbyCommandExecutor {
    @Override
    public boolean onCommand(Player player, Lobby lobby, String[] args) {
        if (args.length <= 1) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.url_required"));
            return false;
        }
        URL url;
        try {
            url = new URL(args[0]);
            var service = PluginCinema.getInstance().mediaResolver.servicesRouter.getService("custom");
            if (service == null) {
                throw new MalformedURLException();
            }
            service.add(lobby, player, url, videoInfo -> {
                videoInfo.title = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                lobby.add(url, videoInfo, player);
            });
        } catch (MalformedURLException e) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.invalid_url"));
            return false;
        }
        return true;
    }
}
