package com.cgvsu.math;


public class Vector3f implements Vector<Vector3f> {
    private float x;
    private float y;
    private float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    @Override
    public boolean epsEquals(Vector3f v) {
        return Math.abs(x - v.getX()) < eps && Math.abs(y - v.getY()) < eps && Math.abs(z - v.getZ()) < eps;
    }

    @Override
    public void add(float a) {
        x += a;
        y += a;
        z += a;
    }

    public void add(Vector3f v) {
        x += v.getX();
        y += v.getY();
        z += v.getZ();
    }
    /*public Vector3f sumWith(Vector3f other) {
        Vector3f a, b;
        Vector3f c = a.sumWith(b).sumWith(a).sumWith(c)
        return new Vector3f(x + other.getX(), )
    }*/

    @Override
    public void subtract(Vector3f v) {
        x -= v.getX();
        y -= v.getY();
        z -= v.getZ();
    }

    @Override
    public void multiply(float a) {
        x *= a;
        y *= a;
        z *= a;
    }

    @Override
    public float getLength() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public boolean isZero() {
        return Math.abs(x - 0.0f) < eps && Math.abs(y - 0.0f) < eps && Math.abs(z - 0.0f) < eps;
    }

    @Override
    public void print() {
        System.out.print(x + " " + y + " " + z);
    }

    public Vector4f toVector4f(float w) {
        return new Vector4f(x, y, z, w);
    }

    public Vector4f toVector4f() {
        return toVector4f(0);
    }

    public void multiply(Matrix3f m) {
        x = x * m.getAt(0, 0) + y * m.getAt(0, 1) + z * m.getAt(0, 2);
        y = x * m.getAt(1, 0) + y * m.getAt(1, 1) + z * m.getAt(1, 2);
        z = x * m.getAt(2, 0) + y * m.getAt(2, 1) + z * m.getAt(2, 2);
    }
    public void multiply(Matrix4f m) {
        x = x * m.getAt(0, 0) + y * m.getAt(0, 1) + z * m.getAt(0, 2)  + m.getAt(0, 3);
        y = x * m.getAt(1, 0) + y * m.getAt(1, 1) + z * m.getAt(1, 2)  + m.getAt(1, 3);
        z = x * m.getAt(2, 0) + y * m.getAt(2, 1) +z * m.getAt(2, 2) + m.getAt(2, 3);
        float w = x * m.getAt(3, 0) + y * m.getAt(3, 1) + z * m.getAt(3, 2) + m.getAt(3, 3);
        x = x/w;
        y = y/w;
        z = z/w;
    }

    public static Vector3f multiply(Vector3f v, Matrix3f m) {
        return new Vector3f(
                v.getX() * m.getAt(0, 0) + v.getY() * m.getAt(0, 1) + v.getZ() * m.getAt(0, 2),
                v.getX() * m.getAt(1, 0) + v.getY() * m.getAt(1, 1) + v.getZ() * m.getAt(1, 2),
                v.getX() * m.getAt(2, 0) + v.getY() * m.getAt(2, 1) + v.getZ() * m.getAt(2, 2));
    }

    public static float dot(final Vector3f v1, final Vector3f v2) {
        return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
    }
    public float dot(final Vector3f v2) {
        return x * v2.getX() + y * v2.getY() + z * v2.getZ();
    }


    public static Vector3f cross(final Vector3f v1, final Vector3f v2) {
        return new Vector3f(v1.getY() * v2.getZ() - v1.getZ() * v2.getY(),
                v2.getX() * v1.getZ() - v2.getZ() * v1.getX(),
                v1.getX() * v2.getY() - v1.getY() * v2.getX());
    }

    public static Vector3f add(final Vector3f v1, final Vector3f v2) {
        return new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    public static Vector3f subtract(final Vector3f v1, final Vector3f v2) {
        return new Vector3f(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    public static Vector3f multiply(final Vector3f v, final float a) {
        return new Vector3f(v.getX() * a, v.getY() * a, v.getZ() * a);
    }
    public static Vector3f multiply(Vector3f v, Matrix4f m) {
        Vector4f v1 =  new Vector4f(
                v.getX() * m.getAt(0, 0) + v.getY() * m.getAt(0, 1) + v.getZ() * m.getAt(0, 2)
                        + m.getAt(0, 3),
                v.getX() * m.getAt(1, 0) + v.getY() * m.getAt(1, 1) + v.getZ() * m.getAt(1, 2)
                        + m.getAt(1, 3),
                v.getX() * m.getAt(2, 0) + v.getY() * m.getAt(2, 1) + v.getZ() * m.getAt(2, 2)
                        + m.getAt(2, 3),
                v.getX() * m.getAt(3, 0) + v.getY() * m.getAt(3, 1) + v.getZ() * m.getAt(3, 2)
                        + m.getAt(3, 3)
        );
        return v1.getHomogeneous3f();
    }

    @Override

    public Vector3f clone()  {
        try {
            return (Vector3f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
