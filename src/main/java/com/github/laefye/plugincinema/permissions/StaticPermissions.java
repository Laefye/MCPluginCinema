package com.github.laefye.plugincinema.permissions;

import com.github.laefye.plugincinema.lobby.Lobby;
import org.bukkit.entity.Player;

public class StaticPermissions {
    public enum Permission {
        CINEMA,
    }

    public static boolean hasNotPermission(Permission p, Player player) {
        if (p == Permission.CINEMA) {
            return !player.hasPermission("cinema.cinema");
        }
        return true;
    }
}
