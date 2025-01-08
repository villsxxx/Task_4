package com.cgvsu.math;

public class AffineTransforms {
    public static Matrix4f scale(float sx, float sy, float sz) {
        return new Matrix4f(new float[][]{
                {sx, 0, 0, 0},
                {0, sy, 0, 0},
                {0, 0, sz, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f rotateX(double cos, double sin) {
        if(!isCosAndSin(cos, sin,0.1f)){
            return Matrix4f.createIdentityMatrix();
        }
        float c = (float) cos;
        float s = (float) sin;
        return new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, c, s, 0},
                {0, -s, c, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f rotateY(double cos, double sin) {
        if(!isCosAndSin(cos, sin, 0.1)){
            return Matrix4f.createIdentityMatrix();
        }
        float c = (float) cos;

        float s = (float) sin;
        return new Matrix4f(new float[][]{
                {c, 0, s, 0},
                {0, 1, 0, 0},
                {-s, 0, c, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f rotateZ(double cos, double sin) {
        if(!isCosAndSin(cos, sin, 0.1f)){
            return Matrix4f.createIdentityMatrix();
        }
        float c = (float) cos;
        float s = (float) sin;
        return new Matrix4f(new float[][]{
                {c, s, 0, 0},
                {-s, c, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f translate(float tx, float ty, float tz) {
        return new Matrix4f(new float[][]{
                {1, 0, 0, tx},
                {0, 1, 0, ty},
                {0, 0, 1, tz},
                {0, 0, 0, 1}
        });
    }

    public static void scale(Vector4f v, float sx, float sy, float sz) {
        v.multiply(AffineTransforms.scale(sx, sy, sz));
    }

    //против часовой
    public static void rotateX(Vector4f v, double cos, double sin) {
        v.multiply(AffineTransforms.rotateX(cos, sin));
    }

    public static void rotateY(Vector4f v, double cos, double sin) {
        v.multiply(AffineTransforms.rotateY(cos, sin));
    }

    public static void rotateZ(Vector4f v, double cos, double sin) {
        v.multiply(AffineTransforms.rotateZ(cos, sin));
    }

    public static void translate(Vector4f v, float tx, float ty, float tz) {
        v.multiply(AffineTransforms.translate(tx, ty, tz));
    }
    private static boolean isCosAndSin(double a, double b, double eps){
        if(Math.abs(a*a + b*b - 1) > eps){
            return false;
        }
        if(Double.compare(a, 1)>0 || Double.compare(a, -1)<0){
            return false;
        }
        if(Double.compare(b, 1)>0 || Double.compare(b, -1)<0){
            return false;
        }
        return true;
    }
}

