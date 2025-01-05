package com.cgvsu.math;

public class AffineTransforms {
    public static void scale(Vector4f v, float sx, float sy, float sz) {
        v.multiply(new Matrix4f(new float[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        }));
    }

    //против часовой
    public static void rotateX(Vector4f v, float cos, float sin) {
        v.multiply(new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, cos, sin, 0},
                {0, -sin, cos, 0},
                {0, 0, 0, 1}
        }));
    }

    public static void rotateY(Vector4f v, float cos, float sin) {
        v.multiply(new Matrix4f(new float[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        }));
    }

    public static void rotateZ(Vector4f v, float cos, float sin) {
        v.multiply(new Matrix4f(new float[][]{
                {cos, sin, 0, 0},
                {-sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        }));
    }

    public static void translate(Vector4f v, float tx, float ty, float tz) {
        v.multiply(new Matrix4f(new float[][]{
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        }));
    }
}

