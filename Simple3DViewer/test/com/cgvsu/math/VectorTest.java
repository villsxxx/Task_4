package com.cgvsu.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VectorTest {
    @Test
    public void VectorAddTest1() {
        final Vector3f result = new Vector3f(3, 6, 8);
        result.add(6);
        final Vector3f expectedResult = new Vector3f(9, 12, 14);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

    @Test
    public void VectorTesNormaliseTest() {
        final Vector3f result = new Vector3f(0, 0, 0);
        final Vector3f expectedResult = new Vector3f(0, 0, 0);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void VectorMultiplyTest() {
        final Vector3f result = new Vector3f(3, 6, 8);
        result.multiply(new Matrix3f());
        final Vector3f expectedResult = new Vector3f(0, 0, 0);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void VectorAddTest2() {
        final Vector3f result = new Vector3f(3, 6, 8);
        result.add(new Vector3f(0, 5, 0));
        final Vector3f expectedResult = new Vector3f(3, 11, 8);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
}
