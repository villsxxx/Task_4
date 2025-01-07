package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class PolygonRasterizer {
    private BufferedImage frameBuffer; // Буфер для хранения пикселей (изображение)
    protected float[][] zBuffer; // Z-буфер для хранения глубины пикселей
    private int width, height; // Ширина и высота изображения

    public PolygonRasterizer(int width, int height) {
        this.width = width;
        this.height = height;
        this.frameBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.zBuffer = new float[width][height];
        clearBuffers(); // Очистка буферов при создании
    }

    // Метод для очистки буферов
    private void clearBuffers() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                frameBuffer.setRGB(x, y, new Color(0, 0, 0, 0).getRGB()); // Установка полностью прозрачного цвета
                zBuffer[x][y] = Float.NEGATIVE_INFINITY; // Инициализация Z-буфера
            }
        }
    }

    // Метод для растеризации треугольника
    public void rasterizeTriangle(Vector3f v1, Vector3f v2, Vector3f v3, Color color) {
        // Сортировка вершин по Y для упрощения растеризации
        if (v1.getY() > v2.getY()) {
            Vector3f temp = v1;
            v1 = v2;
            v2 = temp;
        }
        if (v2.getY() > v3.getY()) {
            Vector3f temp = v2;
            v2 = v3;
            v3 = temp;
        }
        if (v1.getY() > v2.getY()) {
            Vector3f temp = v1;
            v1 = v2;
            v2 = temp;
        }

        // Расчет высот треугольника
        float totalHeight = v3.getY() - v1.getY();

        for (float y = v1.getY(); y <= v3.getY(); y++) {
            boolean secondHalf = y > v2.getY() || v2.getY() == v1.getY();
            int segmentHeight = secondHalf ? (int) (v3.getY() - v2.getY()) : (int) (v2.getY() - v1.getY());
            float alpha = (y - v1.getY()) / totalHeight;
            float beta = (y - (secondHalf ? v2.getY() : v1.getY())) / segmentHeight;

            Vector3f A = Vector3f.add(v1, Vector3f.multiply(Vector3f.subtract(v2, v1), alpha)); // Левый край
            Vector3f B = secondHalf
                    ? Vector3f.add(v2, Vector3f.multiply(Vector3f.subtract(v3, v2), beta))
                    : Vector3f.add(v1, Vector3f.multiply(Vector3f.subtract(v3, v1), alpha));

            if (A.getX() > B.getX()) {
                Vector3f temp = A;
                A = B;
                B = temp;
            }

            for (int x = (int) A.getX(); x <= (int) B.getX(); x++) {
                float z = interpolateZ(A, B, x); // Интерполяция Z-координаты

                if (x >= 0 && x < width && (int) y >= 0 && (int) y < height && z > zBuffer[x][(int) y]) {
                    frameBuffer.setRGB(x, (int) y, color.getRGB()); // Установка цвета пикселя
                    zBuffer[x][(int) y] = z; // Обновление Z-буфера
                }
            }
        }
    }

    // Метод для интерполяции Z-координаты между двумя вершинами
    private float interpolateZ(Vector3f A, Vector3f B, int x) {
        float t = (x - A.getX()) / (B.getX() - A.getX());
        return A.getZ() + t * (B.getZ() - A.getZ()); // Линейная интерполяция Z
    }


    // Метод для получения текущего изображения из буфера
    public BufferedImage getFrameBuffer() {
        return frameBuffer;
    }
}