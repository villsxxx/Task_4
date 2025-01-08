package com.cgvsu.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatrixTest {
    @Test
    public void ZeroMatrixTest() {
        final Matrix3f result = new Matrix3f();
        final Matrix3f expectedResult = new Matrix3f(new float[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0},
        });
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

    @Test
    public void MatrixVectorMultiplyTest() {
        final Vector3f result = new Vector3f(1, 2, -1);
        result.multiply(new Matrix3f(new float[][]{
                {2, -1, 3},
                {4, 2, 0},
                {-1, 1, 1}
        }));
        final Vector3f expectedResult = new Vector3f(-3, 8, 0);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

    @Test
    public void MatrixMultiplyTest1() {
        final Matrix3f result = Matrix3f.multiplication(new Matrix3f(new float[][]{
                {1, 0, 2},
                {-1, 3, 0},
                {2, 1, 3}}), new Matrix3f(new float[][]{
                {3, -1, 2},
                {-4, 0, 2},
                {1, 1, 2}
        }));

        final Matrix3f expectedResult = new Matrix3f(new float[][]{
                {5, 1, 6},
                {-15, 1, 4},
                {5, 1, 12}
        });
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

    @Test
    public void Matrix3fTest1() {
        try {
            final Matrix3f result = new Matrix3f(new float[][]{
                    {3, -1, 2},
                    {-4, 0, 2, 4},
                    {1, 1, 2},
            });
            Assertions.fail();
        } catch (Exception e) {
            String expectedError = "Matrix must be 3 by 3";
            Assertions.assertEquals(expectedError, e.getMessage());
        }
    }
    @Test
    public void Matrix3fTest2() {
        try {
            final Matrix3f result = new Matrix3f(new float[][]{
                    {3, -1, 2},
                    {-4, 0, 2},
                    {1, 1, 2},
                    {1, 2, 4}
            });
            Assertions.fail();
        } catch (Exception e) {
            String expectedError = "Matrix must be 3 by 3";
            Assertions.assertEquals(expectedError, e.getMessage());
        }
    }
    @Test
    public void MatrixIdentityTest1() {
        final Matrix4f result = new Matrix4f();
        result.makeIdentityMatrix();

        final Matrix4f expectedResult = new Matrix4f(new float[][]{
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

    @Test
    public void MatrixIdentityTest2() {
        final Matrix3f result = Matrix3f.createIdentityMatrix();
        final Matrix3f expectedResult = new Matrix3f(new float[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1},
        });
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void zeroMatrixTest1() {
        final Matrix4f result = new Matrix4f();
        final Matrix4f expectedResult = new Matrix4f(new float[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        });
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void MultiplyMatrixTest2() {
        Matrix3f matrix1 = new Matrix3f(new float[][]{
                {1, 0, 2},
                {-1, 3, 0},
                {2, 1, 3}});
        Matrix3f matrix2 = new Matrix3f(new float[][]{
                {3, -1, 2},
                {-4, 0, 2},
                {1, 1, 2}
        });
        Matrix3f result = matrix1.getMultiplicationWith(matrix2);
        final Matrix3f expectedResult = new Matrix3f(new float[][]{
                {5, 1, 6},
                {-15, 1, 4},
                {5, 1, 12}
        });
        //matrix1.print();
        //matrix2.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

}
