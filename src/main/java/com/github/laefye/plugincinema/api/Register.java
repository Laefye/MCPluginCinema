package com.github.laefye.plugincinema.api;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.order.AbstractOrder;
import com.github.laefye.plugincinema.permissions.PermManager;
import com.github.laefye.plugincinema.utils.CinemaCommand;
import com.github.laefye.plugincinema.utils.Pair;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Register {
    public final HashMap<String, Function<Lobby, PermManager>> permManagerBuilders = new HashMap<>();
    public final HashMap<String, Function<Lobby, AbstractOrder>> orderBuilders = new HashMap<>();
    public final HashMap<String, CinemaCommand> cinemaCommands = new HashMap<>();
    public final ArrayList<Pair<EventType, Consumer<Player>>> events = new ArrayList<>();

    public void registerPermManagerBuilder(Function<Lobby, PermManager> builder, String id) {
        permManagerBuilders.put(id, builder);
    }

    public void registerOrderBuilder(Function<Lobby, AbstractOrder> builder, String id) {
        orderBuilders.put(id, builder);
    }

    public void registerCinemaCommand(CinemaCommand command, String id) {
        cinemaCommands.put(id, command);
    }

    public void registerEvent(EventType eventType, Consumer<Player> callback) {
        events.add(new Pair<>(eventType, callback));
    }

    public Optional<Function<Lobby, PermManager>> getPermManagerBuilder(String id) {
        return Optional.ofNullable(permManagerBuilders.get(id));
    }

    public Optional<Function<Lobby, AbstractOrder>> getOrderBuilder(String id) {
        return Optional.ofNullable(orderBuilders.get(id));
    }

    public Optional<CinemaCommand> getCinemaCommand(String id) {
        return Optional.ofNullable(cinemaCommands.get(id));
    }

    public void executeEvent(EventType eventType, Player player) {
        events.stream()
                .filter(pair -> pair.getFirst() == eventType)
                .forEach(pair -> pair.getSecond().accept(player));
    }
}
