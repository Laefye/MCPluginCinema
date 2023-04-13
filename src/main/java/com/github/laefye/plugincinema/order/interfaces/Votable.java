package com.github.laefye.plugincinema.order.interfaces;

import com.github.laefye.plugincinema.order.Voting;

public interface Votable {
    enum VotingType {
        SKIP
    }

    Voting getVoting(VotingType type);
}
