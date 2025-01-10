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
        int u = (int) Math.round(texture[0] * (mesh.imageToText.wight - 1));
        int v = (int) Math.round(texture[1] * (mesh.imageToText.height - 1));
        if (u < mesh.imageToText.wight && v < mesh.imageToText.height) {
            rgb[0] = mesh.imageToText.pixelData[u][v][0];
            rgb[1] = mesh.imageToText.pixelData[u][v][1];
            rgb[2] = mesh.imageToText.pixelData[u][v][2];
        }
    }

    public Image getImage() {
        return getImage();
    }
}