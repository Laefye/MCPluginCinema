package com.github.laefye.plugincinema.lobby;

import com.github.laefye.cinema.packet.AbstractPacket;
import com.github.laefye.cinema.packet.S2CSync;
import com.github.laefye.cinema.packet.S2CUrl;
import com.github.laefye.cinema.types.ServerScreenInfo;
import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.order.AbstractOrder;
import com.github.laefye.plugincinema.order.LinkedOrder;
import com.github.laefye.plugincinema.order.OrderEntry;
import com.github.laefye.plugincinema.order.interfaces.Addable;
import com.github.laefye.plugincinema.order.interfaces.Listable;
import com.github.laefye.plugincinema.permissions.PermManager;
import com.github.laefye.videoapi.VideoInfo;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class Lobby {
    public ArrayList<Player> players = new ArrayList<>();
    public AbstractOrder order;
    public PermManager permManager = null;
    public LobbyInfo lobbyInfo;
    public String uuid = UUID.randomUUID().toString();

    public Lobby(LobbyInfo lobbyInfo) {
        this.lobbyInfo = lobbyInfo;
        order = new LinkedOrder(this);
        PluginCinema.getInstance().register.getOrderBuilder(LinkedOrder.ID).ifPresent(builder -> order = builder.apply(this));
        PluginCinema.getInstance().register.getPermManagerBuilder(lobbyInfo.permManagerId).ifPresent(builder -> permManager = builder.apply(this));
    }

    public ServerScreenInfo getServerInfo() {
        return new ServerScreenInfo(
            lobbyInfo.screenInfo,
            uuid
        );
    }

    public void alertPlayers(AbstractPacket packet) {
        for (var player : players) {
            PluginCinema.getInstance().send(player, packet);
        }
    }

    public void join(Player player) {
        var previousLobby = PluginCinema.getInstance().getLobbyByPlayer(player);
        if (previousLobby != null) {
            previousLobby.exit(player);
        }

        PluginCinema.getInstance().send(player, createUrlPacket());

        players.add(player);
    }

    public void exit(Player player) {
        var packet = new S2CUrl();
        packet.init(uuid, "");
        PluginCinema.getInstance().send(player, packet);
        players.remove(player);
    }

    public S2CUrl createUrlPacket() {
        var packet = new S2CUrl();
        var element = order.active();
        if (element == null) {
            packet.init(uuid, "");
        } else {
            packet.init(uuid, element.videoInfo.url);
        }
        return packet;
    }

    public void tellPlayers(BaseComponent[] components) {
        for (var player : players) {
            player.spigot().sendMessage(components);
        }
    }

    public void rewind(long millis) {
        var active = order.active();
        if (active == null || active.videoInfo.duration == -1) {
            return;
        }
        active.startedWatching -= millis;
        active.createTask();
        alertPlayers(createSyncPacket());
    }

    public S2CSync createSyncPacket() {
        var active = order.active();
        if (active == null || active.videoInfo.duration == -1) {
            return null;
        }
        var packet = new S2CSync();
        packet.init(uuid, System.currentTimeMillis() - active.startedWatching);
        return packet;
    }

    public void updateBossbar() {
        var bar = order.recalculateBossbar();
        if (bar == null) {
            return;
        }
        for (var player : players) {
            bar.addPlayer(player);
        }
        var previousPlayers = new ArrayList<>(bar.getPlayers());
        for (var player : previousPlayers) {
            if (!players.contains(player)) {
                bar.removePlayer(player);
            }
        }
    }

    public <T> T getOrderType(Class<T> face) {
        if (!face.isAssignableFrom(order.getClass())) {
            return null;
        }
        return (T) order;
    }

    public <T> T getOrderType(Class<T> face, Player player) {
        T e = getOrderType(face);
        if (e == null) {
            PluginCinema.getInstance().execute(() -> player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.not_valid_order")));
            return null;
        }
        return e;
    }

    public void add(URL url, VideoInfo videoInfo, Player player) {
        PluginCinema.getInstance().execute(() -> {
            if (getOrderType(Addable.class, player) == null || getOrderType(Listable.class, player) == null) {
                return;
            }
            var addable = getOrderType(Addable.class, player);
            var listable = getOrderType(Listable.class, player);
            if (videoInfo == null) {
                PluginCinema.getInstance().execute(() -> player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.invalid_url")));
                return;
            }
            if (listable.has(url)) {
                player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.already_added"));
                return;
            }
            addable.add(new OrderEntry(videoInfo, url, player.getName(), order));
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.status.added_to_order"));
        });
    }
}
