package com.github.laefye.cinema.packet;

import com.github.laefye.cinema.wrapper.WrappedPacketByteBuf;
import com.github.laefye.cinema.types.ScreenInfo;
import com.github.laefye.cinema.types.ServerScreenInfo;

import java.util.ArrayList;
import java.util.List;

public class S2CInitScreens extends AbstractPacket {
    public void init(List<ServerScreenInfo> infoList) {
        super.init(PacketCommand.S2C_INIT_SCREENS);
        buffer.writeInt(infoList.size());
        for (var info : infoList) {
            writeString(info.uuid);
            buffer.writeFloat(info.info.width);
            buffer.writeFloat(info.info.height);
            buffer.writeDouble(info.info.x);
            buffer.writeDouble(info.info.y);
            buffer.writeDouble(info.info.z);
            buffer.writeDouble(info.info.rotation);
            buffer.writeFloat(info.info.distance);
        }
    }

    public ArrayList<ServerScreenInfo> serverScreenInfoList;

    @Override
    public void parse(WrappedPacketByteBuf buffer) {
        super.parse(buffer);
        int count = buffer.readInt();
        serverScreenInfoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var uuid = readString();
            var width = buffer.readFloat();
            var height = buffer.readFloat();
            var x = buffer.readDouble();
            var y = buffer.readDouble();
            var z = buffer.readDouble();
            var rotation = buffer.readDouble();
            var distance = buffer.readFloat();
            serverScreenInfoList.add(new ServerScreenInfo(
                    new ScreenInfo(width, height, x, y, z, rotation, distance),
                    uuid
            ));
        }
    }
}
