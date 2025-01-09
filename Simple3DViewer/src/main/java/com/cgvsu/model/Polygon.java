package com.cgvsu.model;

import com.cgvsu.objreader.exceptions.FaceWordException;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;
    private int lineIndex;

    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        this.normalIndices = normalIndices;
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь хотя бы 3 вершины.");
        }
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        if (textureVertexIndices.size() < 3) {
            throw new IllegalArgumentException("Полигоны с текстурами должны иметь хотя бы 3 текстурные вершины.");
        }
        this.textureVertexIndices = textureVertexIndices;
    }
/*
    public void setNormalIndices(ArrayList<Integer> normalIndices) {
        if (normalIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь хотя бы 3 нормали.");
        }
        this.normalIndices = normalIndices;
    }
*/
    public ArrayList<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public ArrayList<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public ArrayList<Integer> getNormalIndices() {
        return normalIndices;
    }

    public boolean hasTexture() {
        return !textureVertexIndices.isEmpty();
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public void checkIndices(int verticesSize, int textureVerticesSize, int normalsSize) {
        for (int i = 0; i < vertexIndices.size(); i++) {
            int vertexIndex = vertexIndices.get(i);
            if (vertexIndex >= verticesSize || vertexIndex < 0) {
                throw new FaceWordException("vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < textureVertexIndices.size(); i++) {
            int textureVertexIndex = textureVertexIndices.get(i);
            if (textureVertexIndex >= textureVerticesSize || textureVertexIndex < 0) {
                throw new FaceWordException("texture vertex", lineIndex, i + 1);
            }
        }

        for (int i = 0; i < normalIndices.size(); i++) {
            int normalIndex = normalIndices.get(i);
            if (normalIndex >= normalsSize || normalIndex < 0) {
                throw new FaceWordException("normal", lineIndex, i + 1);
            }
        }
    }
    @Override
    public Polygon clone() {
        try {
            Polygon clonedPolygon = (Polygon) super.clone();

            // Глубокое копирование списков
            clonedPolygon.vertexIndices = new ArrayList<>(this.vertexIndices);
            clonedPolygon.textureVertexIndices = new ArrayList<>(this.textureVertexIndices);
            clonedPolygon.normalIndices = new ArrayList<>(this.normalIndices);

            return clonedPolygon;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Этот код никогда не должен выполняться
        }
    }
}

//Заменены assert на выброс IllegalArgumentException.
//Добавлены сообщения, которые помогут понять, почему было выброшено исключение.