package com.github.laefye.plugincinema;

import com.github.laefye.cinema.packet.*;
import com.github.laefye.cinema.server.dispatcher.Dispatcher;
import com.github.laefye.cinema.wrapper.WrappedPacketByteBufs;
import com.github.laefye.plugincinema.api.Register;
import com.github.laefye.plugincinema.commands.*;
import com.github.laefye.plugincinema.config.LangConfig;
import com.github.laefye.plugincinema.config.LobbyConfig;
import com.github.laefye.plugincinema.config.Config;
import com.github.laefye.plugincinema.events.DisconnectListener;
import com.github.laefye.plugincinema.events.dispatcher.InitScreens;
import com.github.laefye.plugincinema.events.InventoryEvents;
import com.github.laefye.plugincinema.events.dispatcher.Sync;
import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.order.LinkedOrder;
import com.github.laefye.plugincinema.permissions.defaults.DefaultPermManager;
import com.github.laefye.plugincinema.api.CinemaAPI;
import com.github.laefye.plugincinema.utils.CinemaCommand;
import com.github.laefye.plugincinema.api.ui.Menu;
import com.github.laefye.videoapi.MediaResolver;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;

public final class PluginCinema extends JavaPlugin implements PluginMessageListener {
    public static String CHANNEL = id("channel");
    public static PluginCinema instance;
    public Dispatcher dispatcher = new Dispatcher();
    public LangConfig lang;
    public LobbyConfig lobbyConfig;
    public Config config;
    public MediaResolver mediaResolver;
    public HashMap<String, Lobby> lobbies = new HashMap<>();


    private void loadConfig() {
        config = new Config();
        lang = new LangConfig();
        mediaResolver = new MediaResolver(config.getFfprobe());

        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);
        getServer().getPluginManager().registerEvents(new DisconnectListener(), this);

        lobbyConfig = new LobbyConfig();
    }

    private void registerCommands() {
        command("play").executor(new CustomPlay());
        command("join").full(new Join());
        command("skip").executor(new Skip());
        command("exit").executor((sender, command, label, args) -> {
            var lobby = getLobbyByPlayer((Player) sender);
            if (lobby == null) {
                return false;
            }
            lobby.exit((Player) sender);
            sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.status.disconnect_from_lobby"));
            return true;
        });
        command("order").executor(new OrderList());
        command("rewind").executor((sender, command, label, args) -> {
            int seconds = 15;
            if (args.length == 1) {
                try {
                    seconds = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.invalid_argument"));
                    return false;
                }
            }
            var lobby = getLobbyByPlayer((Player) sender);
            if (lobby == null) {
                sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.need_join"));
                return false;
            }
            if (!lobby.permManager.rewind((Player) sender)) {
                return false;
            }
            var active = lobby.order.active();
            if (active == null) {
                sender.spigot().sendMessage(PluginCinema.getInstance().lang.getAsComponent("video.error.order_empty"));
                return false;
            }
            lobby.rewind(seconds * 1000L);
            return true;
        });
        command("cinema").full(new Cinema("{{VERSION}}"));
    }

    private void registerEvents() {
        dispatcher.add(AbstractPacket.PacketCommand.C2S_INIT_SCREENS, new InitScreens());
        dispatcher.add(AbstractPacket.PacketCommand.C2S_SYNC, new Sync());

        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
    }

    public HashSet<CinemaAPI.CinemaModule> modules = new HashSet<>();

    private void loadModule(String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            var pm = clazz.getConstructor().newInstance();
            if (!(pm instanceof CinemaAPI.CinemaModule pms)) {
                return;
            }
            pms.onEnable(this);
            modules.add(pms);
            getLogger().info("Loaded module: " + classname);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ignored) {
            PluginCinema.getInstance().getLogger().log(Level.WARNING, "Invalid module \"%s\"".formatted(classname));
        }
    }

    public ArrayList<Menu> menus = new ArrayList<>();
    public Register register = new Register();

    private void register() {
        register.registerPermManagerBuilder(lobby -> new DefaultPermManager().setLobby(lobby), DefaultPermManager.ID);
        register.registerOrderBuilder(LinkedOrder::new, LinkedOrder.ID);
        register.registerCinemaCommand(new CinemaCommand().full(new CinemaGetConfig()), CinemaGetConfig.ID);
        register.registerCinemaCommand(new CinemaCommand().full(new CinemaSetOrder()), CinemaSetOrder.ID);
        register.registerCinemaCommand(new CinemaCommand().full(new CinemaSetPerm()), CinemaSetPerm.ID);
        for (var module : modules) {
            module.register(register);
        }
    }

    @Override
    public void onEnable() {
        instance = this;

        register();
        registerCommands();
        registerEvents();
        loadConfig();

        for (var lobbyInfo : lobbyConfig.getLobbiesInfo()) {
            lobbies.put(lobbyInfo.name, new Lobby(lobbyInfo));
        }

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (var lobby : lobbies.values()) {
                lobby.updateBossbar();
            }
        }, 10L, 0L);
    }

    @Override
    public void onDisable() {
        for (var r : modules) {
            r.onDisable();
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        dispatcher.call(WrappedPacketByteBufs.wrap(message), this, player);
    }

    public void send(Player player, AbstractPacket packet) {
        getServer().getScheduler().runTaskLater(this, () -> {
            player.sendPluginMessage(this, CHANNEL, packet.getBuffer().output.toByteArray());
        }, 5);
    }

    public void execute(Runnable runnable) {
        getServer().getScheduler().runTask(this, runnable);
    }

    public static String id(String path) {
        return "cinema:" + path;
    }

    public CinemaCommand command(String command) {
        return new CinemaCommand(getCommand(command));
    }

    public Lobby getLobbyByScreenUUID(String uuid) {
        for (var value : lobbies.values()) {
            if (value.getServerInfo().uuid.equals(uuid)) {
                return value;
            }
        }
        return null;
    }

    public Lobby getLobbyByPlayer(Player player) {
        for (var value : lobbies.values()) {
            if (value.players.contains(player)) {
                return value;
            }
        }
        return null;
    }

    public static PluginCinema getInstance() {
        return instance;
    }

    public Menu getMenuByInventory(Inventory inventory) {
        for (var menu : menus) {
            if (menu.inventory == inventory) {
                return menu;
            }
        }
        return null;
    }
}
