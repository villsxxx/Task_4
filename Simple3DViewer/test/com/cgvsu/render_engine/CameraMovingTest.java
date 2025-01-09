package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CameraMovingTest {
    @Test
    public void MoveOnScrollTest1() {
         Camera camera = new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);
         Vector3f expectedResult = new Vector3f(0, 5, 100);
         camera.movePosition(new Vector3f(0, 5, 0));
         Vector3f result = camera.getPosition();
         result.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void MoveOnScrollTest2() {
        Camera camera = new Camera(
                new Vector3f(0, 5, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        Vector3f expectedResult = new Vector3f(0, 10, 100);
        camera.movePosition(new Vector3f(0, 5, 0));
        Vector3f result = camera.getPosition();
        result.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
    @Test
    public void MoveOnScrollTest3() {
        Camera camera = new Camera(
                new Vector3f(0, 5, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        Vector3f expectedResult = new Vector3f(0, 10, 100);
        camera.movePosition(new Vector3f(0, 5, 0));
        Vector3f result = camera.getPosition();
        result.print();
        Assertions.assertTrue(result.epsEquals(expectedResult));
    }
}
