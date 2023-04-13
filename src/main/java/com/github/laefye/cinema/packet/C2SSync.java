package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;

public class C2SSync extends AbstractPacket {
    public String uuid;

    @Override
    public void parse(WrappedPacketByteBuf buffer) {
        super.parse(buffer);
        uuid = readString();
    }

    public void init(String uuid) {
        super.init(PacketCommand.C2S_SYNC);
        writeString(uuid);
    }
}
