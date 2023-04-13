package com.github.laefye.cinema.wrapper;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import java.io.EOFException;

public class WrappedPacketByteBuf {
    public ByteArrayDataOutput output;
    public ByteArrayDataInput input;

    public long readLong() {
        try {
            return input.readLong();
        } catch (Exception e) {
            return 0;
        }
    }

    public void writeLong(long value) {
        output.writeLong(value);
    }

    public int readInt() {
        try {
            return input.readInt();
        } catch (Exception e) {
            return 0;
        }
    }

    public void writeInt(int value) {
        output.writeInt(value);
    }

    public void writeBytes(byte[] bytes, int beginIndex, int length) {
        for (int i = beginIndex; i < beginIndex + length; i++) {
            output.writeByte(bytes[i]);
        }
    }
    
    public void readBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = input.readByte();
        }
    }

    public void writeFloat(float value) {
        output.writeFloat(value);
    }

    public void writeDouble(double value) {
        output.writeDouble(value);
    }

    public float readFloat() {
        return input.readFloat();
    }

    public double readDouble() {
        return input.readDouble();
    }
}
