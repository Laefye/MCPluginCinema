package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;

public class S2CSync extends AbstractPacket {
    public String uuid;
    public long millis;

    public void init(String uuid, long millis) {
        super.init(PacketCommand.S2C_SYNC);
        writeString(uuid);
        buffer.writeLong(millis);
    }

    public void parse(WrappedPacketByteBuf buffer) {
        super.parse(buffer);
        uuid = readString();
        millis = buffer.readLong();
    }
}
