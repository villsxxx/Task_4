package com.cgvsu.TL;

import com.cgvsu.math.Vector3f;
import javafx.scene.paint.Color;

public class Light {
    private Vector3f position; // Положение источника света
    private Color color;       // Цвет света
    private double intensity;  // Интенсивность света

    public Light(Vector3f position, Color color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    // Геттеры и сеттеры
    public Vector3f getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    final static double k = 0.5;

    public static void calculateLight(int[] rgb, double[] light, Vector3f normal){
        double l = -(light[0] * normal.getX() + light[1] * normal.getY() + light[2] * normal.getZ());
        if(l < 0){
            l = 0;
        }
        rgb[0] = Math.min(255, (int) (rgb[0] * (1 - k) + rgb[0] * k * l));
        rgb[1] = Math.min(255, (int) (rgb[1] * (1 - k) + rgb[1] * k * l));
        rgb[2] = Math.min(255, (int) (rgb[2] * (1 - k) + rgb[2] * k * l));
    }

    public static int[] getGradientCoordinatesRGB(final double[] baristicCoords, final Color[] color) {
        int r = Math.min(255, (int) Math.abs(color[0].getRed() * 255 * baristicCoords[0] + color[1].getRed()
                * 255 * baristicCoords[1] + color[2].getRed() * 255 * baristicCoords[2]));
        int g = Math.min(255, (int) Math.abs(color[0].getGreen() * 255 * baristicCoords[0] + color[1].getGreen()
                * 255 * baristicCoords[1] + color[2].getGreen() * 255 * baristicCoords[2]));
        int b = Math.min(255, (int) Math.abs(color[0].getBlue() * 255 * baristicCoords[0] + color[1].getBlue()
                * 255 * baristicCoords[1] + color[2].getBlue() * 255 * baristicCoords[2]));

        return new int[]{r, g, b};
    }

    public static Vector3f smoothingNormal(final double[] baristicCoords, final Vector3f[] normals) {
        return new Vector3f((float) (baristicCoords[0] * normals[0].getX() + baristicCoords[1] * normals[1].getX() + baristicCoords[2] * normals[2].getX()),
                (float) (baristicCoords[0] * normals[0].getY() + baristicCoords[1] * normals[1].getY() + baristicCoords[2] * normals[2].getY()),
                (float) (baristicCoords[0] * normals[0].getZ() + baristicCoords[1] * normals[1].getZ() + baristicCoords[2] * normals[2].getZ()));
    }

}
