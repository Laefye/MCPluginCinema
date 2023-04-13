package com.github.laefye.plugincinema.config;

import java.io.File;

public class Config extends GeneralConfig {
    public Config() {
        super(new File("plugins/cinema/config.yml"));
        put("api.ffprobe", "plugins/cinema/ffprobe");
        put("cinema.on_old_version", "warning");

        load();
        save();
    }

    public String getFfprobe() {
        return get("api.ffprobe");
    }

    public String getOnOldVersion() {
        return get("api.on_old_version");
    }
}
