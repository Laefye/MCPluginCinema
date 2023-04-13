package com.github.laefye.plugincinema.order;

import java.util.HashSet;

public class Voting {
    public AbstractOrder order;
    private final HashSet<String> votes = new HashSet<>();

    public Voting(AbstractOrder order) {
        this.order = order;
    }

    public boolean isVoted(String username) {
        return votes.contains(username);
    }

    public void vote(String username) {
        votes.add(username);
    }

    public boolean calculateResult() {
        return votes.size() >= getNumberOfVotes();
    }

    private double getPercentage() {
        return 0.5;
    }

    public int getNumberOfVotes() {
        return (int) Math.ceil(getPercentage() * order.getLobby().players.size());
    }

    public int size() {
        return votes.size();
    }

    public void reset() {
        votes.clear();
    }
}
