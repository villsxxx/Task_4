package com.cgvsu.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AffineTransformsTest {
    @Test
    public void ScaleTest1() {
        Vector4f result = new Vector4f(5, 1, 3, 1);
        float sx = 3;
        float sy = 2;
        float sz = 1;
        Vector4f expectedResult = new Vector4f(15, 2, 3, 1);
        result.scale(sx, sy, sz);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void ScaleTest2() {
        Vector4f result = new Vector4f(5, 1, 3, 1);
        float sx = 0;
        float sy = 0;
        float sz = 0;
        Vector4f expectedResult = new Vector4f(0, 0, 0, 1);
        result.scale(sx, sy, sz);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateXTest1() {
        Vector4f result = new Vector4f(5, 0, 0, 1);
        double cos = 0.1;
        double sin = 0.99;
        Vector4f expectedResult = new Vector4f(5, 0, 0, 1);
        result.rotateX(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateXTest2() {
        Vector4f result = new Vector4f(7, 5, 0, 1);
        double cos = 0.766;
        double sin = 0.6428;
        result.print();
        Vector4f expectedResult = new Vector4f(7, 3.83f, -3.2139997f, 1);
        result.rotateX(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateXTest3() {
        Vector4f result = new Vector4f(7, 5, 14, 1);
        double cos = 1;
        double sin = 0;
        Vector4f expectedResult = new Vector4f(7, 5, 14, 1);
        result.rotateX(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateYTest3() {
        Vector4f result = new Vector4f(7, 5, 14, 1);
        double cos = 1;
        double sin = 0;
        Vector4f expectedResult = new Vector4f(7, 5, 14, 1);
        result.rotateY(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateZTest3() {
        Vector4f result = new Vector4f(7, 5, 14, 1);
        double cos = 1;
        double sin = 0;
        Vector4f expectedResult = new Vector4f(7, 5, 14, 1);
        result.rotateZ(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateYTest1() {
        Vector4f result = new Vector4f(0, 5, 0, 1);
        double cos = 0.2;
        double sin = 0.97;
        Vector4f expectedResult = new Vector4f(0, 5, 0, 1);
        result.rotateY(cos, sin);
        result.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateZTest1() {
        Vector4f result = new Vector4f(0, 0, 5, 1);
        double cos = 0.2;
        double sin = 0.96;
        Vector4f expectedResult = new Vector4f(0, 0, 5, 1);
        result.rotateZ(cos, sin);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateTest1() {
        Vector4f result = new Vector4f(20, 14, 15, 1);
        float x = 0;
        float y = 0;
        float z = 0;
        Vector4f expectedResult = new Vector4f(20, 14, 15, 1);
        result.multiply(AffineTransforms.rotateX(Math.cos(x), Math.sin(x)).getMultiplicationWith(
                AffineTransforms.rotateY(Math.cos(y), Math.sin(y)).getMultiplicationWith(
                        AffineTransforms.rotateZ(Math.cos(z), Math.sin(z))
                )));
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void rotateTest2() {
        Vector4f result = new Vector4f(20, 14, 15, 1);
        float x = 0;
        float y = 0;
        float z = 0;
        Vector4f expectedResult = new Vector4f(20, 14, 15, 1);
        result.rotateX(Math.cos(x), Math.sin(x));
        result.rotateY(Math.cos(y), Math.sin(y));
        result.rotateZ(Math.cos(z), Math.sin(z));
        result.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void TranslateTest1() {
        Vector4f result = new Vector4f(5, 1, 3, 1);
        float tx = 0;
        float ty = 0;
        float tz = 0;
        Vector4f expectedResult = new Vector4f(5, 1, 3, 1);
        result.translate(tx, ty, tz);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void TranslateTest2() {
        Vector4f result = new Vector4f(5, 1, 3, 1);
        float tx = 4;
        float ty = 3;
        float tz = 1;
        Vector4f expectedResult = new Vector4f(9, 4, 4, 1);
        result.translate(tx, ty, tz);
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }

}
