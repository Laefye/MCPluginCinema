package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;

public class S2CUrl extends AbstractPacket{
    public String uuid;
    public String url;

    public void init(String uuid, String url) {
        super.init(AbstractPacket.PacketCommand.S2C_URL);
        writeString(uuid);
        writeString(url);
        buffer.writeInt(0); // Временный способ, пока мод не обновлю
    }

    @Override
    public void parse(WrappedPacketByteBuf buffer) {
        super.parse(buffer);
        uuid = readString();
        url = readString();
    }
}
