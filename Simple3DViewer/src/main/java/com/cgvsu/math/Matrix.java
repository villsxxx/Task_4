package ru.vsu.cs.CG2024.RAA.task3.matrix;

import java.util.Arrays;

public abstract class Matrix {
    private float[][] matrix;
    private static final float EPS = 1e-7f;

    Matrix(float[][] matrix) {
        this.matrix = cloneFloatMatrix(matrix);
    }

    Matrix(float[][] matrix, int n) {
        if (!isMatrixSquare(matrix, n)) {
            throw new RuntimeException("Matrix must be " + n + " by " + n);
        }
        this.matrix = cloneFloatMatrix(matrix);
    }

    Matrix() {
    }
    public float[][] getMatrix() {
        return cloneFloatMatrix(matrix);
    }

    public void setMatrix(float[][] matrix) {
        this.matrix = cloneFloatMatrix(matrix);
    }

    public void makeIdentityMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = i == j ? 1 : 0;
            }
        }
    }

    public void print() {
        for (float[] floats : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(floats[j] + " ");
            }
            System.out.println();
        }
    }

    private static float[][] cloneFloatMatrix(float[][] matrix) {
        float[][] matrixCopy = new float[matrix.length][matrix[0].length];
        for (int i = 0; i < matrixCopy.length; i++) {
            matrixCopy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return matrixCopy;
    }

    public <T extends Matrix> boolean epsEquals(T m) {
        for (int i = 0; i < m.getMatrix().length; i++) {
            for (int j = 0; j < m.getMatrix()[0].length; j++) {
                if (Math.abs(m.getMatrix()[i][j] - getMatrix()[i][j]) > EPS) {
                    return false;
                }
            }
        }
        return true;
    }

    public float getAt(int i, int j) {
        return matrix[i][j];
    }

    public void setAt(int i, int j, float a) {
        matrix[i][j] = a;
    }

    public float getAndSetAt(int i, int j, float a) {
        float k = matrix[i][j];
        matrix[i][j] = a;
        return k;
    }

    protected static float[][] sum(float[][] m1, float[][] m2) {
        if (m1.length != m2.length && m1[0].length != m2[0].length) {
            throw new RuntimeException("This matrix can not be added");
        }
        float[][] m = new float[m1.length][m2[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = m1[i][j] + m2[i][j];
            }
        }
        return m;
    }

    public <T extends Matrix> void add(T m) {
        setMatrix(Matrix.sum(getMatrix(), m.getMatrix()));
    }

    protected static float[][] subtraction(float[][] m1, float[][] m2) {
        if (m1.length != m2.length && m1[0].length != m2[0].length) {
            throw new RuntimeException("This matrix can not be subtracted");
        }
        float[][] m = new float[m1.length][m2[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = m1[i][j] - m2[i][j];
            }
        }
        return m;
    }

    public <T extends Matrix> void subtract(T m) {
        setMatrix(Matrix.subtraction(getMatrix(), m.getMatrix()));
    }

    protected static float[][] multiplication(float[][] m1, float[][] m2) {
        if (m1[0].length != m2.length) {
            throw new RuntimeException("This matrix can not be multiplied");
        }
        float[][] m = new float[m1.length][m2[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                m[i][j] = 0;
                for (int k = 0; k < m1[0].length; k++) {
                    m[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return m;
    }

    public <T extends Matrix> void multiply(T m) {
        setMatrix(Matrix.multiplication(getMatrix(), m.getMatrix()));
    }

    protected static float[][] transpose(float[][] m) {
        float[][] tr = new float[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                tr[j][i] = m[i][j];
        return tr;
    }

    public void transpose() {
        setMatrix(Matrix.transpose(getMatrix()));
    }

    protected static boolean isMatrixSquare(float[][] matrix, int n) {
        if (matrix.length != n) {
            return false;
        }
        for (float[] floats : matrix) {
            if (floats.length != n) {
                return false;
            }
        }
        return true;
    }
}


   /* protected static float[][] getZeroMatrix(int raw, int column) {
        float[][] m = new float[column][raw];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                m[j][i] = 0.0f;
        return m;
    }*/
 /*public void setZeroMatrix() {
        this.matrix = Matrix.getZeroMatrix(matrix.length, matrix[0].length);
    }*/