package com.github.laefye.cinema.types;

public class ServerScreenInfo {
    public ScreenInfo info;
    public String uuid;

    public ServerScreenInfo(ScreenInfo info, String uuid) {
        this.info = info;
        this.uuid = uuid;
    }
}
