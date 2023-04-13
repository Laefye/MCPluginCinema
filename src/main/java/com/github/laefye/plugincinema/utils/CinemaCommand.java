package com.github.laefye.plugincinema.utils;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CinemaCommand {
    private PluginCommand command;
    private final boolean isLocal;

    public CinemaCommand(PluginCommand command) {
        this.command = command;
        isLocal = false;
    }

    private CommandExecutor executor;
    private TabCompleter tabCompleter;

    public CinemaCommand() {
        isLocal = true;
    }

    public <T extends CommandExecutor & TabCompleter> CinemaCommand full(T event) {
        return executor(event).tab(event);
    }

    public <T extends CommandExecutor> CinemaCommand executor(T event) {
        if (isLocal) {
            executor = event;
            return this;
        }
        if (command != null) {
            command.setExecutor(event);
        }
        return this;
    }

    public <T extends TabCompleter> CinemaCommand tab(T event) {
        if (isLocal) {
            tabCompleter = event;
            return this;
        }
        if (command != null) {
            command.setTabCompleter(event);
        }
        return this;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public TabCompleter getTabCompleter() {
        return tabCompleter;
    }
}
