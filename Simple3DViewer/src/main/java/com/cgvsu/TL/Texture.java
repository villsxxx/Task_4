package com.cgvsu.TL;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.io.FileInputStream;
import java.io.IOException;

public class Texture {
    private Image textureImage;
    private PixelReader pixelReader;

    public Texture(String filePath) throws IOException {
        loadTexture(filePath);
    }

    private void loadTexture(String filePath) throws IOException {
        textureImage = new Image(new FileInputStream(filePath));
        if (textureImage.isError()) {
            throw new IOException("Не удалось загрузить текстуру из файла: " + filePath);
        }
        pixelReader = textureImage.getPixelReader();
    }

    public int getColor(float u, float v) {
        u = Math.max(0, Math.min(u, 1));
        v = Math.max(0, Math.min(v, 1));

        int x = Math.min((int) (u * textureImage.getWidth()), (int) textureImage.getWidth() - 1);
        int y = Math.min((int) (v * textureImage.getHeight()), (int) textureImage.getHeight() - 1);

        return pixelReader.getArgb(x, y);
    }

    public int getWidth() {
        return (int) textureImage.getWidth();
    }

    public int getHeight() {
        return (int) textureImage.getHeight();
    }
}