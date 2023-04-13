package com.github.laefye.plugincinema.order;

import com.github.laefye.plugincinema.lobby.Lobby;
import com.github.laefye.plugincinema.PluginCinema;
import com.github.laefye.plugincinema.order.interfaces.Addable;
import com.github.laefye.plugincinema.order.interfaces.Listable;
import com.github.laefye.plugincinema.order.interfaces.Votable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

public class LinkedOrder extends AbstractOrder implements Votable, Listable, Addable {
    public static String ID = PluginCinema.id("linked");
    private final LinkedBlockingQueue<OrderEntry> elements = new LinkedBlockingQueue<>();
    private final Voting votingToSkip;

    public LinkedOrder(Lobby lobby) {
        super(lobby);
        this.votingToSkip = new Voting(this);
    }

    @Override
    public OrderEntry active() {
        return elements.peek();
    }

    @Override
    protected Optional<OrderEntry> getNext() {
        if (elements.size() == 0) {
            return Optional.empty();
        }
        elements.remove().cancelTask();
        return Optional.ofNullable(active());
    }

    @Override
    public void onNext() {
        votingToSkip.reset();
    }

    @Override
    public void dispose() {
        super.dispose();
        for (var element : elements) {
            element.cancelTask();
        }
        elements.clear();
    }

    @Override
    public boolean has(URL url) {
        var urlSource = PluginCinema.getInstance().mediaResolver.servicesRouter.getServiceOrCustom(url);
        for (var request : elements) {
            var requestSource = PluginCinema.getInstance().mediaResolver.servicesRouter.getServiceOrCustom(request.publicUrl);
            if (requestSource.getUniqueID(request.publicUrl).equals(urlSource.getUniqueID(url))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(OrderEntry element) {
        elements.add(element);
        if (elements.size() == 1) {
            play();
            votingToSkip.reset();
        }
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public ArrayList<OrderEntry> getEntries() {
        return new ArrayList<>(elements);
    }

    @Override
    public Voting getVoting(VotingType type) {
        if (type == VotingType.SKIP) {
            return votingToSkip;
        }
        return null;
    }
}
