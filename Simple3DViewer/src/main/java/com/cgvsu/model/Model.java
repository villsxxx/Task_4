package com.cgvsu.model;

import com.cgvsu.TL.Texture;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector2f;
import com.cgvsu.TL.Light; // Импортировать класс Light

import java.util.ArrayList;
import javafx.scene.image.Image;

import static com.cgvsu.TL.Light.calculateLight;
import static com.cgvsu.TL.Light.smoothingNormal;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<>();
    public ArrayList<Vector3f> normals = new ArrayList<>();
    public ArrayList<Polygon> polygons = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<>();

    // Поля для текстуры
    private String texturePath;
    private Image textureImage;
    private Texture texture;

    // Поля для материала
    private Vector3f ambientColor;    // Цвет амбиентного освещения
    private Vector3f diffuseColor;    // Цвет диффузного освещения
    private Vector3f specularColor;   // Цвет блестящего освещения
    private float shininess;           // Коэффициент блеска

    public void addVertex(Vector3f vertex) {
        vertices.add(vertex);
    }

    public void addTextureVertex(Vector2f textureVertex) {
        textureVertices.add(textureVertex);
    }

    public void addNormal(Vector3f normal) {
        normals.add(normal);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public Polygon getFirstPolygon() {
        return polygons.get(0);
    }

    // Методы получения информации о размерах списков
    public int getVerticesSize() {
        return vertices.size();
    }

    public int getTextureVerticesSize() {
        return textureVertices.size();
    }

    public int getNormalsSize() {
        return normals.size();
    }

    public int getPolygonsSize() {
        return polygons.size();
    }

    // Методы получения списков
    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public ArrayList<Vector3f> getNormals() {
        return normals;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    // Метод для установки текстуры
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    // Метод для очистки текстуры
    public void clearTexture() {
        this.texture = null;
    }

    // Метод для получения текстуры
    public Texture getTexture() {
        return texture;
    }

    // Методы для установки и получения материала
    public void setMaterialProperties(Vector3f ambient, Vector3f diffuse, Vector3f specular, float shininess) {
        this.ambientColor = ambient;
        this.diffuseColor = diffuse;
        this.specularColor = specular;
        this.shininess = shininess;
    }

    public Vector3f getAmbientColor() {
        return ambientColor;
    }

    public Vector3f getDiffuseColor() {
        return diffuseColor;
    }

    public Vector3f getSpecularColor() {
        return specularColor;
    }

    public float getShininess() {
        return shininess;
    }

    public static void light(final double[] barizentric, final Vector3f[] normals, double[] light, int[] rgb){
        Vector3f smooth = smoothingNormal(barizentric, normals);
        calculateLight(rgb, light, smooth);
    }
}