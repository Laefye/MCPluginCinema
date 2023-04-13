package com.github.laefye.plugincinema.events.dispatcher;

import com.github.laefye.cinema.packet.C2SSync;
import com.github.laefye.cinema.server.dispatcher.IDispatcherEvent;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;

public class Sync implements IDispatcherEvent {
    @Override
    public void on(WrappedPacketByteBuf buf, PluginCinema plugin, Player player) {
        var input = new C2SSync();
        input.parse(buf);
        var lobby = PluginCinema.getInstance().getLobbyByScreenUUID(input.uuid);
        if (lobby != null) {
            var sync = lobby.createSyncPacket();
            if (sync != null)
            {
                plugin.send(player, sync);
            }
        }
    }
}
