package com.github.laefye.cinema.server.dispatcher;

import com.github.laefye.cinema.packet.AbstractPacket;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Dispatcher {
    private static class DispatcherEntry {
        public AbstractPacket.PacketCommand command;
        public IDispatcherEvent event;

        public DispatcherEntry(AbstractPacket.PacketCommand command, IDispatcherEvent event) {
            this.command = command;
            this.event = event;
        }
    }

    private final ArrayList<DispatcherEntry> events = new ArrayList<>();

    public void add(AbstractPacket.PacketCommand command, IDispatcherEvent event) {
        events.add(new DispatcherEntry(command, event));
    }

    public void call(WrappedPacketByteBuf buf, PluginCinema plugin, Player player) {
        var command = AbstractPacket.getCommand(buf.readInt());
        for (var entry : events) {
            if (command == entry.command) {
                entry.event.on(buf, plugin, player);
            }
        }
    }
}
