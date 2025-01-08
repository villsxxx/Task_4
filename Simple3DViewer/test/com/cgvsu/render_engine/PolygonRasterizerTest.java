package com.cgvsu.render_engine;

import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolygonRasterizerTest {
    private PolygonRasterizer rasterizer;

    @BeforeEach
    public void setUp() {
        // Инициализация рендерера с шириной и высотой 10 пикселей
        rasterizer = new PolygonRasterizer(10, 10);
    }

    @Test
    public void testClearBuffers() {
        BufferedImage frameBuffer = rasterizer.getFrameBuffer();

        // Проверка, что все пиксели установлены в полностью прозрачный цвет
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                assertEquals(new Color(0, 0, 0, 0).getRGB(), frameBuffer.getRGB(x, y));
            }
        }
    }

    @Test
    public void testRasterizeTriangle() {
        Vector3f v1 = new Vector3f(2, 2, 0);
        Vector3f v2 = new Vector3f(7, 2, 0);
        Vector3f v3 = new Vector3f(4, 5, 0);
        Color color = Color.RED;

        rasterizer.rasterizeTriangle(v1, v2, v3, color);

        // Проверка цвета пикселей, находящихся внутри треугольника
        BufferedImage frameBuffer = rasterizer.getFrameBuffer();
        assertEquals(color.getRGB(), frameBuffer.getRGB(4, 3)); // Пиксель внутри треугольника
        assertEquals(color.getRGB(), frameBuffer.getRGB(3, 3)); // Пиксель внутри треугольника
        assertEquals(color.getRGB(), frameBuffer.getRGB(5, 3)); // Пиксель внутри треугольника
        assertEquals(color.getRGB(), frameBuffer.getRGB(4, 2)); // Пиксель на нижней границе

        // Проверка пикселей вне треугольника
        assertEquals(new Color(0, 0, 0, 0).getRGB(), frameBuffer.getRGB(0, 0)); // Пиксель вне треугольника
        assertEquals(new Color(0, 0, 0, 0).getRGB(), frameBuffer.getRGB(8, 8)); // Пиксель вне треугольника
    }

    @Test
    public void testZBufferUpdate() {
        Vector3f v1 = new Vector3f(2, 1, 1);
        Vector3f v2 = new Vector3f(4, 1, 2);
        Vector3f v3 = new Vector3f(3, 4, 3);
        Color color = Color.GREEN;

        rasterizer.rasterizeTriangle(v1, v2, v3, color);

        // Проверка обновления Z-буфера для пикселей внутри треугольника
        float[][] zBuffer = rasterizer.zBuffer;
        assertTrue(zBuffer[3][3] > Float.NEGATIVE_INFINITY); // Z значение для точки внутри треугольника
        assertTrue(zBuffer[3][2] > Float.NEGATIVE_INFINITY); // Z значение для крайнего нижнего пикселя
    }
}