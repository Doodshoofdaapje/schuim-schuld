package com.boris.schuimschuld.util;

public class Tuple<X, Y> {
    public final X x;
    public final Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X _x() {
        return this.x;
    }

    public Y _y() {
        return this.y;
    }
}