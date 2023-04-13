package com.github.laefye.plugincinema.lobby;

import com.github.laefye.cinema.types.ScreenInfo;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public class LobbyInfo {
    public String name;
    public ScreenInfo screenInfo;
    public String permManagerId;
    public HashMap<String, ?> permManagerParams = new HashMap<>();

    public LobbyInfo(String name, ScreenInfo info, String permManagerId, HashMap<String, ?> permManagerParams) {
        this.name = name;
        this.screenInfo = info;
        this.permManagerId = permManagerId;
        this.permManagerParams = permManagerParams;
    }

    public LobbyInfo(String name) {
        this.name = name;
    }

    private String suffix(String suffix) {
        return "lobbies." + name + "." + suffix;
    }

    public void read(YamlConfiguration yaml) {
        var width = (float) yaml.getDouble(suffix("screen.width"));
        var height = (float) yaml.getDouble(suffix("screen.height"));
        var x = yaml.getDouble(suffix("screen.position.x"));
        var y = yaml.getDouble(suffix("screen.position.y"));
        var z = yaml.getDouble(suffix("screen.position.z"));
        var rotation = yaml.getDouble(suffix("screen.position.rotation"));
        var distance = yaml.getDouble(suffix("screen.distance"));
        screenInfo = new ScreenInfo(width, height, x, y, z, rotation, (float) distance);
        permManagerId = yaml.getString(suffix("permmanager.id"));
        permManagerParams = (HashMap<String, ?>) yaml.getConfigurationSection(suffix("permmanager.params")).getValues(true);
    }

    public void write(YamlConfiguration yaml) {
        yaml.set(suffix("screen.width"), screenInfo.width);
        yaml.set(suffix("screen.height"), screenInfo.height);
        yaml.set(suffix("screen.position.x"), screenInfo.x);
        yaml.set(suffix("screen.position.y"), screenInfo.y);
        yaml.set(suffix("screen.position.z"), screenInfo.z);
        yaml.set(suffix("screen.position.rotation"), screenInfo.rotation);
        yaml.set(suffix("screen.distance"), screenInfo.distance);
        yaml.set(suffix("permmanager.id"), permManagerId);
        yaml.createSection(suffix("permmanager.params"), permManagerParams);
    }
}