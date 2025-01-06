package com.cgvsu.render_engine;

import com.cgvsu.math.*;

public class GraphicConveyor {

    public static Matrix4f rotateScaleTranslate() {
        float[][] matrix = new float[][]{
                {10, 0, 0, 0},
                {0, 10, 0, 0},
                {0, 0, 10, 0},
                {0, 0, 0, 1}};
        return new Matrix4f(matrix);
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target) {
        return lookAt(eye, target, new Vector3f(0F, 1.0F, 0F));
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f target, Vector3f up) {
        Vector3f resultZ = Vector3f.subtract(target, eye);
        Vector3f resultX = Vector3f.cross(up, resultZ);
        Vector3f resultY = Vector3f.cross(resultZ, resultX);
        resultX.normalize();
        resultY.normalize();
        resultZ.normalize();
        float[][] matrix = new float[][]{
                {resultX.getX(), resultX.getY(), resultX.getZ(), -resultX.dot(eye)},
                {resultY.getX(), resultY.getY(), resultY.getZ(), -resultY.dot(eye)},
                {resultZ.getX(), resultZ.getY(), resultZ.getZ(), -resultZ.dot(eye)},
                {0, 0, 0, 1}};
        return new Matrix4f(matrix);
    }

    public static Matrix4f perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        Matrix4f result = new Matrix4f();
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));
        result.setAt(0,0, tangentMinusOnDegree / aspectRatio);
        result.setAt(1,1, tangentMinusOnDegree);
        result.setAt(2,2, (farPlane + nearPlane) / (farPlane - nearPlane));
        result.setAt(3,2, 1.0F);
        result.setAt(2,3, 2 * (nearPlane * farPlane) / (nearPlane - farPlane));
        return result;
    }
    public static Vector2f vertexToPoint(final Vector3f vertex, final int width, final int height) {
//        return new Vector2f(vertex.getX() * width + width / 2.0F, -vertex.getY() * height + height / 2.0F);
        return new Vector2f((vertex.getX() * (width-1))/2.0F +  (width-1)/2.0F,
                (vertex.getY() * (1-height))/2.0F +  (height-1)/2.0F);
    }
}
