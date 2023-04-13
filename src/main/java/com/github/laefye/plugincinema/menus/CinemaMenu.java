package com.github.laefye.plugincinema.menus;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.api.ui.Menu;
import com.github.laefye.plugincinema.commands.Skip;
import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.order.interfaces.Listable;
import com.github.laefye.plugincinema.order.interfaces.Votable;
import com.github.laefye.plugincinema.permissions.PermManager;
import com.github.laefye.plugincinema.utils.ItemMetaWrapper;
import com.github.laefye.plugincinema.utils.heads.PlayerProfileTools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;

public class CinemaMenu extends Menu {
    protected Lobby lobby;
    protected Player player;

    protected CinemaMenu(Lobby lobby, Player player, String title) {
        super(title);
        this.lobby = lobby;
        this.player = player;
        setVoteItemStack();
        setHowPlayItem();
        setOrderItem();
    }

    public CinemaMenu(Lobby lobby, Player player) {
        this(lobby, player, PluginCinema.getInstance().lang.getPhrase("video.menu.cinema.title"));
        player.openInventory(inventory);
    }

    private void setVoteItemStack() {
        var skipAbility = lobby.permManager.canSkip(player).value;
        if (skipAbility == PermManager.SkipAbility.NONE) {
            return;
        }
        var meta = new ItemMetaWrapper(new ItemStack(Material.FIREWORK_ROCKET))
                .setDisplayName(PluginCinema.getInstance().lang.getPhraseWithReset(
                        skipAbility == PermManager.SkipAbility.SKIP ? "video.menu.cinema.skip" : "video.menu.cinema.vote_to_skip"
                ));
        var vote = lobby.getOrderType(Votable.class);
        if (vote != null) {
            meta.setLore(List.of(
                    PluginCinema.getInstance().lang.getPhraseFormattedWithReset("video.menu.cinema.voted", vote.getVoting(Votable.VotingType.SKIP).size()),
                    PluginCinema.getInstance().lang.getPhraseFormattedWithReset("video.menu.cinema.need_votes", vote.getVoting(Votable.VotingType.SKIP).getNumberOfVotes()))
            );
        }
        inventory.setItem(28, meta.apply());
    }

    public void setHowPlayItem() {
        inventory.setItem(13, new ItemMetaWrapper(new ItemStack(Material.PLAYER_HEAD))
                .setDisplayName(PluginCinema.getInstance().lang.getPhraseWithReset("video.menu.cinema.how_play.title"))
                .setHead("2705fd94a0c431927fb4e639b0fcfb49717e412285a02b439e0112da22b2e2ec")
                .setLore(PluginCinema.getInstance().lang.getLore("video.menu.cinema.how_play.description"))
                .apply());
    }

    public void setOrderItem() {
        if (lobby.getOrderType(Listable.class) == null) {
            return;
        }
        inventory.setItem(31, new ItemMetaWrapper(new ItemStack(Material.BOOK))
                .setDisplayName(PluginCinema.getInstance().lang.getPhraseWithReset("video.menu.cinema.order"))
                .apply());
    }

    @Override
    public void onClick(Player player, int slot) {
        if (slot == 28 && (lobby.getOrderType(Votable.class) != null || lobby.getOrderType(Listable.class) != null) && lobby.permManager.canSkip(player).value != PermManager.SkipAbility.NONE) {
            Skip.skip(player, lobby);
            player.closeInventory();
        }
        if (slot == 31 && lobby.getOrderType(Listable.class) != null) {
            player.closeInventory();
            new OrderMenu(lobby.getOrderType(Listable.class).getEntries(), player);
        }
    }
}
