package com.cgvsu.math.vector;

public interface Vector<T> {
    float eps = 1e-7f;

    boolean epsEquals(T v);

    void add(T v);

    void add(float a);

    void subtract(T v);

    void multiply(float a);

    float getLength();

    boolean isZero();
    void print();

    default void normalize() {
        if (!isZero()) {
            multiply(1 / getLength());
        }
    }
}
