package com.github.laefye.plugincinema.order.interfaces;

import com.github.laefye.plugincinema.order.OrderEntry;

import java.net.URL;
import java.util.ArrayList;

public interface Listable {
    boolean has(URL url);

    int getSize();

    ArrayList<OrderEntry> getEntries();
}
