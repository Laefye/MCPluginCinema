package com.github.laefye.plugincinema.config;

import com.github.laefye.cinema.types.ScreenInfo;
import com.github.laefye.plugincinema.lobby.LobbyInfo;
import com.github.laefye.plugincinema.PluginCinema;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LobbyConfig extends GeneralConfig {
    private static class LobbiesField implements GeneralConfig.IField<ArrayList<LobbyInfo>> {
        private ArrayList<LobbyInfo> lobbies = new ArrayList<>();
        private String key;

        @Override
        public GeneralConfig.IField<ArrayList<LobbyInfo>> fill(String key, ArrayList<LobbyInfo> def) {
            this.key = key;
            this.lobbies = def;
            return this;
        }

        @Override
        public ArrayList<LobbyInfo> get(YamlConfiguration configuration) {
            var lobbiesName = configuration.getConfigurationSection("lobbies").getKeys(false);
            for (var lobbyName : lobbiesName) {
                var lobby = new LobbyInfo(lobbyName);
                lobby.read(configuration);
                lobbies.add(lobby);
            }
            return lobbies;
        }

        @Override
        public void put(YamlConfiguration configuration) {
            for (var lobby : lobbies) {
                lobby.write(configuration);
            }
        }

        @Override
        public ArrayList<LobbyInfo> getValue() {
            return lobbies;
        }

        @Override
        public String getKey() {
            return key;
        }
    }


    public LobbyConfig() {
        super(new File("plugins/cinema/lobbies.yml"));

        var params = new HashMap<String, Object>();
        for (var i : PluginCinema.getInstance().mediaResolver.servicesRouter.services) {
            params.put(i.getName(), true);
        }
        putField("lobbies", new LobbiesField(), new ArrayList<>(
                List.of(
                        new LobbyInfo("default",
                                new ScreenInfo(4, 3, 0, 0, 0, 0, 50),
                                "cinema:default",
                                params
                        )
                )
        ));

        load();
        save();
    }

    public ArrayList<LobbyInfo> getLobbiesInfo() {
        return get("lobbies");
    }
}
