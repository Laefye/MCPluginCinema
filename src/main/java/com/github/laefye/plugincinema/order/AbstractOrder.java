package com.github.laefye.plugincinema.order;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.Optional;

public abstract class AbstractOrder {
    public BossBar bar = Bukkit.createBossBar("Not video", BarColor.RED, BarStyle.SOLID);
    protected Lobby lobby;

    public AbstractOrder(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public abstract OrderEntry active();
    protected abstract Optional<OrderEntry> getNext();

    public void next() {
        var media = getNext();
        if (media.isEmpty()) {
            lobby.alertPlayers(lobby.createUrlPacket());
            return;
        }
        play();
        onNext();
    }

    public void play() {
        if (active() != null && !active().isActive()) {
            active().setActive();
            lobby.alertPlayers(lobby.createUrlPacket());
            tellVideoInfo();
        }
    }

    public abstract void onNext();

    public void tellVideoInfo() {
        var active = active();
        lobby.tellPlayers(
                PluginCinema.getInstance().lang.getAsFormattedBuilder("video.status.playing_video", active.videoInfo.title)
                        .event(new ClickEvent(ClickEvent.Action.OPEN_URL, active.publicUrl.toString()))
                        .create()
        );
    }

    private double clamp(double min, double value, double max) {
        return Math.min(max, Math.max(min, value));
    }

    public BossBar recalculateBossbar() {
        if (active() == null) {
            bar.removeAll();
            return null;
        }
        var duration = active().videoInfo.duration > 0 ?
                1.0 - (double)active().getNotPassedDuration() / (double)active().videoInfo.duration
                : 1;
        bar.setProgress(clamp(0, duration, 1));
        bar.setTitle(active().videoInfo.title);
        return bar;
    }

    public void dispose() {
        bar.removeAll();
    }
}
