package com.github.laefye.plugincinema.config;

import com.github.laefye.plugincinema.PluginCinema;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class LangConfig extends GeneralConfig {
    public LangConfig() {
        super(new File("plugins/cinema/lang.yml"));
        fillKeys();
        load();
        save();
    }

    private void fillKeys() {
        put("video.status.play", "&aVideo was played!");
        put("video.status.join", "&aYou joined to cinema!");
        put("video.error.invalid_url", "&cInvalid url!");
        put("video.error.url_required", "&cUrl required!");
        put("video.admin.skip", "&aSkipped!");
        put("video.status.loading_url", "&aSearching a video...");
        put("video.status.added_to_order", "&aAdded to order!");
        put("video.status.disconnect_from_lobby", "&aDisconnect from lobby!");
        put("video.status.playing_video", "&aRight now playing video \"&d%s&a\"!");
        put("video.error.need_join", "&cYou need to join");
        put("video.error.invalid_lobby_name", "&cInvalid lobby name");
        put("video.error.invalid_argument", "&cInvalid argument");
        put("video.order.who_added", "&aAdded: &b%s");
        put("video.order.menu", "&dOrder");
        put("video.order.link", "&aLink: &b%s");
        put("video.order.timecode", "&aTimecode: &b%s");
        put("video.error.not_enough_permissions", "&cDon't enough permission");
        put("video.status.voted", "&a%s voted to skip (%d/%d)");
        put("video.error.already_voted", "&cYou have already voted!");
        put("video.error.already_added", "&cThis is video has already added in order!");
        put("video.error.not_valid_order", "&cThis lobby don't support adding video in order!");
        put("video.error.kick.old_version", "&cYou have old version");
        put("video.error.order_empty", "&cOrder is empty");
        put("video.menu.choose_translation", "&dChoose translation");
        put("video.menu.choose_season", "&dChoose season");
        put("video.menu.choose_episode", "&dChoose episode");
        put("video.menu.next_page", "Next page");
        put("video.menu.previous_page", "Previous page");
        put("video.menu.current_page", "Current page: %d");
        put("video.menu.cinema.title", "&dCinema");
        put("video.menu.cinema.skip", "Skip");
        put("video.menu.cinema.vote_to_skip", "Vote to skip");
        put("video.menu.cinema.voted", "Voted: %d");
        put("video.menu.cinema.need_votes", "Need votes: %d");
        put("video.menu.cinema.how_play.title", "How to play video");
        put("video.menu.cinema.how_play.description", "You need use &c/play [link]");
        put("video.menu.cinema.order", "Order");

        for (var module : PluginCinema.getInstance().modules) {
            module.registerLang(this);
        }
    }

    public void putPhrase(String key, String def) {
        put(key, def);
    }

    public String getPhrase(String key) {
        return ChatColor.translateAlternateColorCodes('&', get(key) == null ? key : get(key));
    }

    public String getPhraseWithReset(String key) {
        return ChatColor.RESET + getPhrase(key);
    }

    public List<String> getLore(String key) {
        return Arrays.stream(getPhrase(key).split("\n")).map(s -> ChatColor.RESET + s).toList();
    }

    public String getPhraseFormatted(String key, Object... values) {
        return String.format(getPhrase(key), values);
    }

    public String getPhraseFormattedWithReset(String key, Object... values) {
        return String.format(getPhraseWithReset(key), values);
    }


    public BaseComponent[] getAsComponent(String key) {
        return new ComponentBuilder(getPhrase(key)).create();
    }

    public BaseComponent[] getAsFormattedComponent(String key, Object... values) {
        return getAsFormattedBuilder(key, values).create();
    }

    public ComponentBuilder getAsFormattedBuilder(String key, Object... values) {
        return new ComponentBuilder(getPhraseFormatted(key, values));
    }
}
