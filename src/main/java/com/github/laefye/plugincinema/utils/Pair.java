package com.github.laefye.plugincinema.utils;

public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair<?,?> pair) {
            return first.equals(pair.first) && second.equals(pair.second);
        }
        return super.equals(obj);
    }
}
