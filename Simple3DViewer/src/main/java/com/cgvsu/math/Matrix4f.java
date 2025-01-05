package com.cgvsu.math;

public class Matrix4f extends Matrix {
    public Matrix4f(float[][] matrix) {
        super(matrix, 4);
    }

    public Matrix4f() {
        setMatrix(new float[4][4]);
    }

    public static Matrix4f createIdentityMatrix() {
        Matrix4f matrix = new Matrix4f();
        matrix.makeIdentityMatrix();
        return matrix;
    }

    public Matrix4f getSumWith(Matrix4f other) {
        return sum(this, other);
    }

    public static Matrix4f sum(Matrix4f m1, Matrix4f m2) {
        return new Matrix4f(Matrix.sum(m1.getMatrix(), m2.getMatrix()));
    }

    public Matrix4f getSubtractionWith(Matrix4f other) {
        return subtraction(this, other);
    }

    public static Matrix4f subtraction(Matrix4f m1, Matrix4f m2) {
        return new Matrix4f(Matrix.subtraction(m1.getMatrix(), m2.getMatrix()));
    }

    public Matrix4f getMultiplicationWith(Matrix4f other) {
        return multiplication(this, other);
    }

    public static Matrix4f multiplication(Matrix4f m1, Matrix4f m2) {
        return new Matrix4f(Matrix.multiplication(m1.getMatrix(), m2.getMatrix()));
    }

    public static Matrix4f transpose(Matrix4f m) {
        return new Matrix4f(Matrix.transpose(m.getMatrix()));
    }
}
