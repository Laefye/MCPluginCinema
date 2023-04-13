package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;

public class C2SInitScreens extends AbstractPacket {
    public void init(long protocol) {
        super.init(PacketCommand.C2S_INIT_SCREENS);
        buffer.writeLong(protocol);
    }

    public long protocol = 0;

    @Override
    public void parse(WrappedPacketByteBuf buffer) {
        super.parse(buffer);
        protocol = buffer.readLong();
    }
}

