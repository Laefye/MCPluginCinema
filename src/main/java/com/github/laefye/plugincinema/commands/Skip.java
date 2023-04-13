package com.github.laefye.plugincinema.commands;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.order.interfaces.Listable;
import com.github.laefye.plugincinema.order.interfaces.Votable;
import com.github.laefye.plugincinema.permissions.PermManager;
import com.github.laefye.plugincinema.api.LobbyCommandExecutor;
import org.bukkit.entity.Player;

public class Skip extends LobbyCommandExecutor {
    @Override
    public boolean onCommand(Player player, Lobby lobby, String[] args) {
        return skip(player, lobby);
    }


    public static boolean skip(Player player, Lobby lobby) {
        var order = lobby.order;
        if (order.active() == null) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.order_empty"));
            return false;
        }
        var skipAbility = lobby.permManager.skip(player);
        if (skipAbility == PermManager.SkipAbility.SKIP) {
            order.next();
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.admin.skip"));
            return true;
        }
        if (lobby.getOrderType(Votable.class) == null || lobby.getOrderType(Listable.class) == null) {
            player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.not_valid_order"));
            return false;
        }
        var vote = lobby.getOrderType(Votable.class, player).getVoting(Votable.VotingType.SKIP);
        if (skipAbility == PermManager.SkipAbility.VOTE) {
            var username = player.getName();
            if (vote.isVoted(username)) {
                player.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.already_voted"));
                return false;
            }
            vote.vote(username);
            lobby.tellPlayers(
                    PluginCinema.getInstance().lang.getAsFormattedComponent(
                            "video.status.voted",
                            player.getName(),
                            vote.size(), vote.getNumberOfVotes()
                    )
            );
            if (vote.calculateResult()) {
                lobby.tellPlayers(PluginCinema.getInstance().lang.getAsComponent("video.admin.skip"));
                order.next();
            }
            return true;
        }
        return false;
    }
}
