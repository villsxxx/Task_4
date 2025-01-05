package ru.vsu.cs.CG2024.RAA.task3.matrix;

public class Matrix3f extends Matrix {
    public Matrix3f(float[][] matrix) {
        super(matrix, 3);
    }

    public Matrix3f() {
        setMatrix(new float[3][3]);
    }

    public static Matrix3f createIdentityMatrix() {
        Matrix3f matrix = new Matrix3f();
        matrix.makeIdentityMatrix();
        return matrix;
    }

    public Matrix3f getSumWith(Matrix3f other) {
        return sum(this, other);
    }

    public static Matrix3f sum(Matrix3f m1, Matrix3f m2) {
        return new Matrix3f(Matrix.sum(m1.getMatrix(), m2.getMatrix()));
    }

    public Matrix3f getSubtractionWith(Matrix3f other) {
        return subtraction(this, other);
    }

    public static Matrix3f subtraction(Matrix3f m1, Matrix3f m2) {
        return new Matrix3f(Matrix.subtraction(m1.getMatrix(), m2.getMatrix()));
    }

    public Matrix3f getMultiplicationWith(Matrix3f other) {
        return multiplication(this, other);
    }

    public static Matrix3f multiplication(Matrix3f m1, Matrix3f m2) {
        return new Matrix3f(Matrix.multiplication(m1.getMatrix(), m2.getMatrix()));
    }

    public static Matrix3f transpose(Matrix3f m) {
        return new Matrix3f(Matrix.transpose(m.getMatrix()));
    }
}
