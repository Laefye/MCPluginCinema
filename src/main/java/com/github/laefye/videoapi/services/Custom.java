package com.github.laefye.videoapi.services;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.videoapi.AbstractService;
import com.github.laefye.videoapi.VideoInfo;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.function.Consumer;

public class Custom extends AbstractService {
    @Override
    public boolean isLink(URL link) {
        return true;
    }

    public static final String NAME = "custom";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void add(Lobby lobby, Player player, URL link, Consumer<VideoInfo> callback) {
        if (!lobby.permManager.addService(player, this)) {
            return;
        }
        PluginCinema.getInstance().mediaResolver.getVideoFromDirect(link, callback);
        player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.status.loading_url"));
    }

    @Override
    public String getUniqueID(URL link) {
        return link.toString();
    }
}
