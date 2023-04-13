package com.github.laefye.cinema.wrapper;

import com.google.common.io.ByteStreams;

public class WrappedPacketByteBufs {
    public static WrappedPacketByteBuf create() {
        var buf = new WrappedPacketByteBuf();
        buf.output = ByteStreams.newDataOutput();
        return buf;
    }

    public static WrappedPacketByteBuf wrap(byte[] buffer) {
        var buf = new WrappedPacketByteBuf();
        buf.input = ByteStreams.newDataInput(buffer);
        return buf;
    }
}
