package com.cgvsu.model;
import com.cgvsu.TL.ImageToText;
import com.cgvsu.math.AffineTransforms;
import com.cgvsu.math.Matrix4f;
import com.cgvsu.math.Vector3f;
import com.cgvsu.math.Vector2f;
import java.util.*;

public class Model {

    public ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    public ArrayList<Vector2f> textureVertices = new ArrayList<Vector2f>();
    public ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
    public ArrayList<Polygon> polygons = new ArrayList<Polygon>();
    public ImageToText imageToText;
    private ArrayList<Group> groups = new ArrayList<>();

    public boolean viewMesh = true;
    public boolean selected = false;
    public float xSize = 0;
    private Transformation transformation = Transformation.getDefaultTransformation();
    public Model(){

    }

    public Model(ArrayList<Vector3f> vertices, ArrayList<Vector2f> textureVertices, ArrayList<Vector3f> normals,
                 ArrayList<Polygon> polygons, ArrayList<Group> groups, Transformation transformation) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
        this.groups = groups;
        this.transformation = transformation;
    }



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
    public Transformation getTransformation() {
        return transformation;
    }
    public Matrix4f getModelMatrix() {
        return transformation.getTransformation();
    }

    public Model getModelWithTransformations(){
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getTransformation());
            transformVertices.add(vertex);
        }
        return new Model(transformVertices,textureVertices, normals,
                polygons, groups, Transformation.getDefaultTransformation());
    }
    public void saveScale(){
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getScale());
            transformVertices.add(vertex);
        }
        vertices = transformVertices;
        transformation = transformation.getWithDefaultScale();
    }
    public void saveTranslation(){
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getTranslation());
            transformVertices.add(vertex);
        }
        vertices = transformVertices;
        transformation = transformation.getWithDefaultTranslation();
    }
    public void saveRotation(){
        Transformation oldTransformation = transformation;
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getRotation());
            transformVertices.add(vertex);
        }
        vertices = transformVertices;
        transformation = transformation.getWithDefaultRotation();
    }
    public Model getModelWithTranslation(){
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getTranslation());
            transformVertices.add(vertex);
        }
        return new Model(transformVertices,textureVertices, normals,
                polygons, groups, transformation.getWithDefaultTranslation());
    }
    public Model getModelWithRotation(){
        Transformation oldTransformation = transformation;
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getRotation());
            transformVertices.add(vertex);
        }
        return new Model(transformVertices,textureVertices, normals,
                polygons, groups, transformation.getWithDefaultRotation());
    }
    public Model getModelWithScale(){
        ArrayList<Vector3f> transformVertices = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);
            vertex.multiply(transformation.getScale());
            transformVertices.add(vertex);
        }
        return new Model(transformVertices,textureVertices, normals,
                polygons, groups, transformation.getWithDefaultScale());
    }
    public void setDefaultTransformation(){
        transformation = Transformation.getDefaultTransformation();
    }

    // Метод клонирования вершин
    public ArrayList<Vector3f> cloneVertices() {
        ArrayList<Vector3f> clonedVertices = new ArrayList<>();
        for (Vector3f vertex : this.vertices) {
            clonedVertices.add(vertex.clone());
        }
        return clonedVertices;
    }

    // Метод клонирования текстурных вершин
    public ArrayList<Vector2f> cloneTextureVertices() {
        ArrayList<Vector2f> clonedTextureVertices = new ArrayList<>();
        for (Vector2f textureVertex : this.textureVertices) {
            clonedTextureVertices.add(textureVertex.clone());
        }
        return clonedTextureVertices;
    }

    // Метод клонирования нормалей
    public ArrayList<Vector3f> cloneNormals() {
        ArrayList<Vector3f> clonedNormals = new ArrayList<>();
        for (Vector3f normal : this.normals) {
            clonedNormals.add(normal.clone());
        }
        return clonedNormals;
    }

    // Метод клонирования полигонов
    public ArrayList<Polygon> clonePolygons() {
        ArrayList<Polygon> clonedPolygons = new ArrayList<>();
        for (Polygon polygon : this.polygons) {
            clonedPolygons.add(polygon.clone());
        }
        return clonedPolygons;
    }

    // Метод clone
    @Override
    public Model clone() {
        Model clonedModel = new Model();
        clonedModel.vertices = this.cloneVertices();
        clonedModel.textureVertices = this.cloneTextureVertices();
        clonedModel.normals = this.cloneNormals();
        clonedModel.polygons = this.clonePolygons();
        return clonedModel;
    }
    public void exportToOBJ() {
        // Устанавливаем локаль для использования точки как разделителя дробной части
        Locale.setDefault(Locale.US);

        // Вывод вершин
        for (Vector3f vertex : vertices) {
            System.out.printf("v %.6f %.6f %.6f%n", vertex.getX(), vertex.getY(), vertex.getZ());
        }

        // Вывод нормалей
        for (Vector3f normal : normals) {
            System.out.printf("vn %.6f %.6f %.6f%n", normal.getX(), normal.getY(), normal.getZ());
        }

        // Вывод текстурных координат
        for (Vector2f textureVertex : textureVertices) {
            System.out.printf("vt %.6f %.6f%n", textureVertex.getX(), textureVertex.getY());
        }

        // Вывод полигонов
        for (Polygon polygon : polygons) {
            System.out.print("f");
            for (int i = 0; i < polygon.getVertexIndices().size(); i++) {
                int vertexIndex = polygon.getVertexIndices().get(i) + 1; // Индексация в OBJ начинается с 1
                String facePart = String.valueOf(vertexIndex);

                if (!polygon.getTextureVertexIndices().isEmpty()) {
                    int textureIndex = polygon.getTextureVertexIndices().get(i) + 1;
                    facePart += "/" + textureIndex;
                }

                if (!polygon.getNormalIndices().isEmpty()) {
                    int normalIndex = polygon.getNormalIndices().get(i) + 1;
                    facePart += (facePart.contains("/") ? "" : "/") + "/" + normalIndex;
                }

                System.out.print(" " + facePart);
            }
            System.out.println();
        }
    }
}
