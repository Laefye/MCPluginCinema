package com.github.laefye.plugincinema.api;

import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.config.LangConfig;
import net.md_5.bungee.api.chat.BaseComponent;

public class CinemaAPI {
    public static class PermManagerResult<T> {
        public BaseComponent[] text = PluginCinema.getInstance().lang.getAsComponent("video.error.not_enough_permissions");
        public T value;

        public PermManagerResult<T> setText(BaseComponent[] text) {
            this.text = text;
            return this;
        }

        public static <T> PermManagerResult<T> of(Class<T> clazz, T value) {
            var result = new PermManagerResult<T>();
            result.value = value;
            return result;
        }
    }

    public static abstract class CinemaModule {
        public PluginCinema cinema;

        public void onEnable(PluginCinema cinema) {
            this.cinema = cinema;
        }

        public void onDisable() {

        }

        public abstract void registerLang(LangConfig config);
        public abstract void register(Register register);
    }
}
