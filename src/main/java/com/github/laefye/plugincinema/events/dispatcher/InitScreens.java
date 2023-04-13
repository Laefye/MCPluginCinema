package com.github.laefye.plugincinema.events.dispatcher;

import com.github.laefye.cinema.packet.C2SInitScreens;
import com.github.laefye.cinema.packet.S2CInitScreens;
import com.github.laefye.cinema.server.dispatcher.IDispatcherEvent;
import com.github.laefye.cinema.types.ServerScreenInfo;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InitScreens implements IDispatcherEvent {
    public static long PROTOCOL = 1;

    @Override
    public void on(WrappedPacketByteBuf buf, PluginCinema plugin, Player player) {
        var request = new C2SInitScreens();
        request.parse(buf);

        if (PROTOCOL > request.protocol) {
            if (PluginCinema.getInstance().config.getOnOldVersion().equals("kick")) {
                player.kickPlayer(PluginCinema.getInstance().lang.getPhrase("video.error.kick.old_version"));
            } else if (PluginCinema.getInstance().config.getOnOldVersion().equals("warning")) {
                player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.kick.old_version"));
            }
            return;
        }

        var infoCollection = new ArrayList<ServerScreenInfo>();
        for (var lobby : plugin.lobbies.values()) {
            infoCollection.add(lobby.getServerInfo());
        }
        var packet = new S2CInitScreens();
        packet.init(infoCollection);
        plugin.send(player, packet);
    }
}
