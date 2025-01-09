package com.cgvsu.math;

public class Vector2f implements Vector<Vector2f> {
    private float x;
    private float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
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

    @Override
    public boolean epsEquals(Vector2f v) {
        return Math.abs(x - v.getX()) < eps && Math.abs(y - v.getY()) < eps;
    }

    @Override
    public void add(Vector2f v) {
        x += v.getX();
        y += v.getY();
    }

    @Override
    public void add(float a) {
        x += a;
        y += a;
    }

    @Override
    public void subtract(Vector2f v) {
        x -= v.getX();
        y -= v.getY();
    }

    @Override
    public void multiply(float a) {
        x *= a;
        y *= a;
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public boolean isZero() {
        return Math.abs(x - 0.0f) < eps && Math.abs(y - 0.0f) < eps;
    }

    @Override
    public void print() {
        System.out.print(x + " " + y);
    }

    public Vector3f toVector3f(float z) {
        return new Vector3f(x, y, z);
    }

    public Vector3f toVector3f() {
        return toVector3f(0);
    }

    public static float dot(final Vector2f v1, final Vector2f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY();
    }

    public static Vector2f add(final Vector2f v1, final Vector2f v2) {
        return new Vector2f(v1.getX() + v2.getX(), v1.getY() + v2.getY());
    }

    public static Vector2f subtract(final Vector2f v1, final Vector2f v2) {
        return new Vector2f(v1.getX() - v2.getX(), v1.getY() - v2.getY());
    }

    public static Vector2f multiply(final Vector2f v, final float a) {
        return new Vector2f(v.getX() * a, v.getY() * a);
    }
    @Override
    public Vector2f clone()  {
        try {
            return (Vector2f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}


