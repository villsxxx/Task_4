package com.cgvsu.TL;

import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;



public class ImageToText {

    public static int[][][] pixelData;
    public static int wight;
    public static int height;
    private static Model mesh;

    public void loadImage(String path) {

        BufferedImage img;

        try {
            img = ImageIO.read(new File(path));

            pixelData = new int[img.getWidth()][img.getHeight()][3];
            wight = img.getWidth();
            height = img.getHeight();
            int[] rgb;

            for(int i = 0; i < img.getWidth(); i++){  //для лица и куба
                for(int j = 0; j < img.getHeight(); j++){
                    rgb = getPixelData(img, i, j);
                    pixelData[img.getWidth() - 1 - i][img.getHeight() - 1 - j][0] = rgb[0];
                    pixelData[img.getWidth() - 1 - i][img.getHeight() - 1 - j][1] = rgb[1];
                    pixelData[img.getWidth() - 1 - i][img.getHeight() - 1 - j][2] = rgb[2];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getPixelData(BufferedImage img, int x, int y) {
        int argb = img.getRGB(x, y);

        return new int[] {
                (argb >> 16) & 0xff, //red
                (argb >>  8) & 0xff, //green
                (argb      ) & 0xff  //blue
        };
    }
    public static void applyTexture(Vector2f textureCoords, Model mesh, int[] rgb) {
        ImageToText.mesh = mesh;
        int u = (int) (textureCoords.getX() * wight);
        int v = (int) (textureCoords.getY() * height);

        if (u >= 0 && u < wight && v >= 0 && v < height) {
            rgb[0] = pixelData[u][v][0];
            rgb[1] = pixelData[u][v][1];
            rgb[2] = pixelData[u][v][2];
        }

    }
}