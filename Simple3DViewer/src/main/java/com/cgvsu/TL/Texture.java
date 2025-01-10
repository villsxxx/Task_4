package com.cgvsu.TL;


import com.cgvsu.math.Vector2f;
import com.cgvsu.model.Model;
import javafx.scene.image.Image;

public class Texture {

    public static double[] getGradientCoordinatesTexture(double[] barizentric, Vector2f[] texture) {
        return new double[] {(barizentric[0] * texture[0].getX()) +  (barizentric[1] * texture[1].getX()) +  (barizentric[2] * texture[2].getX()),
                (barizentric[0] * texture[0].getY()) + (barizentric[1] * texture[1].getY()) + (barizentric[2] * texture[2].getY())};
    }

    public static void texture(double[] barizentric, Vector2f[] textures, Model mesh, int[] rgb){
        double[] texture = getGradientCoordinatesTexture(barizentric, textures);
        int u = (int) Math.round(texture[0] * (ImageToText.wight - 1));
        int v = (int) Math.round(texture[1] * (ImageToText.height - 1));
        if (u < ImageToText.wight && v < ImageToText.height) {
            rgb[0] = ImageToText.pixelData[u][v][0];
            rgb[1] = ImageToText.pixelData[u][v][1];
            rgb[2] = ImageToText.pixelData[u][v][2];
        }
    }

    public Image getImage() {
        return getImage();
    }
}