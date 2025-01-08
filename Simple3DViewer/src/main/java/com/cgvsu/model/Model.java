package com.cgvsu.model;
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
    private ArrayList<Group> groups = new ArrayList<>();

    public boolean viewMesh = true;
    public boolean selected = false;
    public float xSize = 0;
    private Transformation transformation = new Transformation(
            AffineTransforms.scale(1, 1, 1),
            AffineTransforms.rotateY(0, 0),
            AffineTransforms.translate(0, 0, 0)
    );
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
                polygons, groups, transformation);
    }

}
