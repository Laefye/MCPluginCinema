package com.github.laefye.cinema.types;

import com.github.laefye.cinema.wrapper.WrappedVec3d;

public class ScreenInfo {
    public float width;
    public float height;
    public double x;
    public double y;
    public double z;
    public float distance;

    public double rotation;

    public ScreenInfo(float width, float height, double x, double y, double z, double rotation, float distance) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.distance = distance;
    }

    public WrappedVec3d location() {
        return new WrappedVec3d(x, y, z);
    }

}