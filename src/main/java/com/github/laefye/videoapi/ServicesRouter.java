package com.github.laefye.videoapi;

import com.github.laefye.videoapi.services.*;

import java.net.URL;
import java.util.ArrayList;

public class ServicesRouter {
    public ArrayList<AbstractService> services = new ArrayList<>();

    public ServicesRouter() {
        services.add(new Custom());
    }

    public AbstractService getService(URL url) {
        for (var service : services) {
            if (service.isLink(url)) {
                return service;
            }
        }
        return null;
    }

    public AbstractService getServiceOrCustom(URL url) {
        for (var service : services) {
            if (service.isLink(url)) {
                return service;
            }
        }
        return getService(Custom.NAME);
    }

    public AbstractService getService(String name) {
        for (var service : services) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        return null;
    }
}
