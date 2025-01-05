package com.cgvsu.math.vector;

import ru.vsu.cs.CG2024.RAA.task3.matrix.Matrix4f;

public class Vector4f implements Vector<Vector4f> {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }

    @Override
    public boolean epsEquals(Vector4f v) {
        return Math.abs(x - v.getX()) < eps && Math.abs(y - v.getY()) < eps &&
                Math.abs(z - v.getZ()) < eps && Math.abs(w - v.getW()) < eps;
    }

    @Override
    public void add(float a) {
        x += a;
        y += a;
        z += a;
        w += a;
    }

    @Override
    public void add(Vector4f v) {
        x += v.getX();
        y += v.getY();
        z += v.getZ();
        w += v.getW();
    }

    @Override
    public void subtract(Vector4f v) {
        x -= v.getX();
        y -= v.getY();
        z -= v.getZ();
        w -= v.getW();
    }

    @Override
    public void multiply(float a) {
        x *= a;
        y *= a;
        z *= a;
        w *= a;
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    @Override
    public boolean isZero() {
        return Math.abs(x - 0.0f) < eps && Math.abs(y - 0.0f) < eps && Math.abs(z - 0.0f) < eps
                && Math.abs(w - 0.0f) < eps;
    }

    @Override
    public void print() {
        System.out.print(x + " " + y + " " + z + " " + w);
    }

    public void multiply(Matrix4f m) {
        Vector4f v = multiply(this, m);
        setX(v.getX());
        setY(v.getY());
        setZ(v.getZ());
        setW(v.getW());
    }

    public static Vector4f multiply(Vector4f v, Matrix4f m) {
        float[][] matrix = m.getMatrix();
        return new Vector4f(
                v.getX() * m.getAt(0, 0) + v.getY() * m.getAt(0, 1) +
                        v.getZ() * m.getAt(0, 2) + v.getW() * m.getAt(0, 3),
                v.getX() * m.getAt(1, 0) + v.getY() * m.getAt(1, 1) +
                        v.getZ() * m.getAt(1, 2) + v.getW() * m.getAt(1, 3),
                v.getX() * m.getAt(2, 0) + v.getY() *  m.getAt(2, 1) +
                        v.getZ() * m.getAt(2, 2) + v.getW() *  m.getAt(2, 3),
                v.getX() *  m.getAt(3, 0) + v.getY() * m.getAt(3, 1) +
                        v.getZ() * m.getAt(3, 2) + v.getW() * m.getAt(3, 3));
    }

    public static float dot(final Vector4f v1, final Vector4f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ() + v1.getW() * v2.getW();
    }

    public static Vector4f add(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.getX() + v2.getX(), v1.getY() + v2.getY(),
                v1.getZ() + v2.getY(), v1.getW() + v2.getW());
    }

    public static Vector4f subtract(final Vector4f v1, final Vector4f v2) {
        return new Vector4f(v1.getX() - v2.getX(), v1.getY() - v2.getY(),
                v1.getZ() - v2.getY(), v1.getW() - v2.getW());
    }

    public static Vector4f multiply(final Vector4f v, final float a) {
        return new Vector4f(v.getX() * a, v.getY() * a, v.getZ() * a, v.getW() * a);
    }
}
