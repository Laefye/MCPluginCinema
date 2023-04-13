package com.github.laefye.plugincinema.permissions;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.api.CinemaAPI;
import com.github.laefye.videoapi.AbstractService;
import org.bukkit.entity.Player;

public abstract class PermManager {
    protected Lobby lobby;

    public PermManager setLobby(Lobby lobby) {
        this.lobby = lobby;
        return this;
    }

    protected abstract CinemaAPI.PermManagerResult<Boolean> canJoin(Player player);

    protected abstract CinemaAPI.PermManagerResult<Boolean> canAddService(Player player, AbstractService service);

    public abstract CinemaAPI.PermManagerResult<SkipAbility> canSkip(Player player);

    protected abstract CinemaAPI.PermManagerResult<Boolean> canRewind(Player player);

    public boolean join(Player player) {
        var canJoin = lobby.permManager.canJoin(player);
        if (!canJoin.value) {
            player.spigot().sendMessage(canJoin.text);
            return false;
        }
        return true;
    }

    public enum SkipAbility {
        NONE,
        VOTE,
        SKIP
    }

    public SkipAbility skip(Player player) {
        var canSkip = lobby.permManager.canSkip(player);
        if (canSkip.value == SkipAbility.NONE) {
            player.spigot().sendMessage(canSkip.text);
            return SkipAbility.NONE;
        }
        return canSkip.value;
    }

    public boolean rewind(Player player) {
        var canRewind = lobby.permManager.canRewind(player);
        if (!canRewind.value) {
            player.spigot().sendMessage(canRewind.text);
            return false;
        }
        return true;
    }

    public boolean addService(Player player, AbstractService service) {
        var canAddService = lobby.permManager.canAddService(player, service);
        if (!canAddService.value) {
            player.spigot().sendMessage(canAddService.text);
            return false;
        }
        return true;
    }
}
