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
    public static Matrix4f rotateX(float cos, float sin) {
        if(!isCosAndSin(cos, sin)){
            return Matrix4f.createIdentityMatrix();
        }
        return new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, cos, sin, 0},
                {0, -sin, cos, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f rotateY(float cos, float sin) {
        if(!isCosAndSin(cos, sin)){
            return Matrix4f.createIdentityMatrix();
        }
        return new Matrix4f(new float[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f rotateZ(float cos, float sin) {
        if(!isCosAndSin(cos, sin)){
            return Matrix4f.createIdentityMatrix();
        }
        return new Matrix4f(new float[][]{
                {cos, sin, 0, 0},
                {-sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }
    public static Matrix4f translate( float tx, float ty, float tz) {
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
    public static void rotateX(Vector4f v, float cos, float sin) {
        v.multiply(AffineTransforms.rotateX(cos, sin));
    }

    public static void rotateY(Vector4f v, float cos, float sin) {
        v.multiply(AffineTransforms.rotateY(cos, sin));
    }

    public static void rotateZ(Vector4f v, float cos, float sin) {
        v.multiply(AffineTransforms.rotateZ(cos, sin));
    }

    public static void translate(Vector4f v, float tx, float ty, float tz) {
        v.multiply(AffineTransforms.translate(tx, ty, tz));
    }
    private static boolean isCosAndSin(float a, float b){
        if(Float.compare(a*a + b*b, 1)!=0){
            return false;
        }
        if(Float.compare(a, 1)>0 || Float.compare(a, -1)<0){
            return false;
        }
        if(Float.compare(b, 1)>0 || Float.compare(b, -1)<0){
            return false;
        }
        return true;
    }
}

