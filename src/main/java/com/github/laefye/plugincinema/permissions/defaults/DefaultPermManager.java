package com.github.laefye.plugincinema.permissions.defaults;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.permissions.PermManager;
import com.github.laefye.plugincinema.api.CinemaAPI;
import com.github.laefye.videoapi.AbstractService;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DefaultPermManager extends PermManager {
    private final HashMap<AbstractService, Boolean> services = new HashMap<>();
    private final HashMap<String, Boolean> players = new HashMap<>();

    @Override
    public CinemaAPI.PermManagerResult<Boolean> canJoin(Player player) {
        return CinemaAPI.PermManagerResult.of(Boolean.class, player.hasPermission("cinema." + lobby.lobbyInfo.name + ".join"));
    }

    @Override
    public CinemaAPI.PermManagerResult<PermManager.SkipAbility> canSkip(Player player) {
        if (player.hasPermission("cinema." + lobby.lobbyInfo.name + ".skip")) {
            return CinemaAPI.PermManagerResult.of(PermManager.SkipAbility.class, SkipAbility.SKIP);
        }
        if (player.hasPermission("cinema." + lobby.lobbyInfo.name + ".vote.skip")) {
            return CinemaAPI.PermManagerResult.of(PermManager.SkipAbility.class, SkipAbility.VOTE);
        }
        return CinemaAPI.PermManagerResult.of(PermManager.SkipAbility.class, SkipAbility.NONE);
    }

    @Override
    public CinemaAPI.PermManagerResult<Boolean> canRewind(Player player) {
        return CinemaAPI.PermManagerResult.of(Boolean.class, player.hasPermission("cinema." + lobby.lobbyInfo.name + ".rewind"));
    }

    @Override
    public CinemaAPI.PermManagerResult<Boolean> canAddService(Player player, AbstractService service) {
        if (players.containsKey(player.getName())) {
            return CinemaAPI.PermManagerResult.of(Boolean.class, players.get(player.getName()));
        }
        return CinemaAPI.PermManagerResult.of(Boolean.class, services.get(service));
    }

    public void addPlayer(String player, boolean value) {
        players.put(player, value);
    }

    public void resetPlayer(String player) {
        players.remove(player);
    }

    @Override
    public PermManager setLobby(Lobby lobby) {
        super.setLobby(lobby);
        for (var s : PluginCinema.getInstance().mediaResolver.servicesRouter.services) {
            services.put(s, lobby.lobbyInfo.permManagerParams.containsKey(s.getName()) && (boolean) lobby.lobbyInfo.permManagerParams.get(s.getName()));
        }
        return this;
    }

    public static String ID = PluginCinema.id("default");

    public void setService(AbstractService service, Boolean value) {
        services.put(service, value);
    }
}
