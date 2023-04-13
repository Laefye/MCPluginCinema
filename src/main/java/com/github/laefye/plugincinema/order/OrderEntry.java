package com.github.laefye.plugincinema.order;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.videoapi.VideoInfo;
import org.bukkit.scheduler.BukkitTask;

import java.net.URL;

public class OrderEntry {
    public VideoInfo videoInfo;
    public URL publicUrl;
    public String username;
    public long startedWatching = 0;
    public BukkitTask task;
    public AbstractOrder order;

    public OrderEntry(VideoInfo info, URL publicUrl, String username, AbstractOrder order) {
        this.videoInfo = info;
        this.publicUrl = publicUrl;
        this.username= username;
        this.order = order;
    }

    public void setActive() {
        startedWatching = System.currentTimeMillis();
        createTask();
        this.order.getLobby().alertPlayers(this.order.getLobby().createUrlPacket());
    }

    public void createTask() {
        cancelTask();
        if (this.videoInfo.duration > 0) {
            task = PluginCinema.getInstance().getServer().getScheduler().runTaskLater(PluginCinema.getInstance(), () -> order.next(), (getNotPassedDuration() + 5) * 20L);
        }
    }

    public void cancelTask() {
        if (task != null) {
            task.cancel();
        }
    }

    public long getNotPassedDuration() {
        var delta = (System.currentTimeMillis() - startedWatching) / 1000;
        return videoInfo.duration - delta;
    }

    public Timecode getTimecode() {
        return new Timecode((System.currentTimeMillis() - startedWatching) / 1000);
    }

    public boolean isActive() {
        return startedWatching != 0;
    }
}
