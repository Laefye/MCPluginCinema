package com.github.laefye.plugincinema.api.ui;

import java.util.ArrayList;

public class Pages<T> {
    private final ArrayList<T> elements;
    private final int size;
    private int offset;


    public Pages(ArrayList<T> elements, int size) {
        this.elements = elements;
        this.size = size;
    }

    public int getCountPages() {
        return (int) Math.ceil((double) elements.size() / size);
    }

    public boolean hasNext() {
        return size + offset < elements.size();
    }

    public boolean hasPrevious() {
        return offset > 0;
    }

    public T get(int i) {
        if (offset + i >= elements.size()) {
            return null;
        }
        return elements.get(offset + i);
    }

    public int getCountInPage() {
        return Math.min(elements.size() - offset, size);
    }

    public void nextPage() {
        if (hasNext()) {
            offset += size;
        }
    }

    public void previousPage() {
        if (hasPrevious()) {
            offset -= size;
        }
    }

    public int getCurrentPage() {
        return offset / size + 1;
    }
}
