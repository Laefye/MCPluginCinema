package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBufs;

import java.nio.charset.StandardCharsets;

public class AbstractPacket {
    public enum PacketCommand {
        S2C_INIT_SCREENS,
        C2S_INIT_SCREENS,
        S2C_URL,
        C2S_SYNC,
        S2C_SYNC,
    }

    protected PacketCommand command;
    protected WrappedPacketByteBuf buffer;

    public void init(PacketCommand command) {
        buffer = WrappedPacketByteBufs.create();
        buffer.writeInt(command.ordinal());
    }

    public void parse(WrappedPacketByteBuf buffer) {
        this.buffer = buffer;
    }

    public WrappedPacketByteBuf getBuffer() {
        return buffer;
    }

    protected void writeString(String string) {
        buffer.writeInt(string.length());
        var bytes = string.getBytes(StandardCharsets.UTF_16);
        buffer.writeBytes(bytes, 2, bytes.length - 2); // Stange bug
    }

    protected String readString() {
        var length = buffer.readInt();
        var bytes = new byte[length * 2];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_16);
    }

    public static PacketCommand getCommand(int v) {
        return PacketCommand.values()[v];
    }
}
