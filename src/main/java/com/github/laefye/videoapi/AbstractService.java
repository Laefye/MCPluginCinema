package com.github.laefye.videoapi;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.function.Consumer;

public abstract class AbstractService {
    public abstract boolean isLink(URL link);
    public abstract String getName();
    public abstract String getUniqueID(URL link);

    public void add(Lobby lobby, Player player, URL link) {
        add(lobby, player, link, videoInfo -> lobby.add(link, videoInfo, player));
    }

    public void add(Lobby lobby, Player player, URL link, Consumer<VideoInfo> callback) {
        if (!lobby.permManager.addService(player, this)) {
            return;
        }
//        PluginCinema.getInstance().minidl.getVideoInfoThroughMiniDl(link, callback);
        player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.status.loading_url"));
    }

    public static final String UNKNOWN_ICON  = "2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec";

    public String getIconTexture() {
        return UNKNOWN_ICON;
    }
}
