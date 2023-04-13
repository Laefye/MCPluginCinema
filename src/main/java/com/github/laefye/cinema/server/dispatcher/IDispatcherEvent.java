package com.github.laefye.cinema.server.dispatcher;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;

public interface IDispatcherEvent {
    void on(WrappedPacketByteBuf buf, PluginCinema plugin, Player player);
}
