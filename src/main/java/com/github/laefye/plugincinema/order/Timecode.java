package com.github.laefye.plugincinema.order;

public class Timecode {
    public int minutes;
    public int seconds;

    public Timecode(long seconds) {
        minutes = (int) (seconds / 60);
        this.seconds = (int) (seconds % 60);
    }

    @Override
    public String toString() {
        return minutes + ":" + String.format("%02d", seconds);
    }
}
