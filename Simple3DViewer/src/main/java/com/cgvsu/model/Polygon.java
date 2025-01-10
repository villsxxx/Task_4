package com.cgvsu.model;

import com.cgvsu.objreader.exceptions.FaceWordException;

import java.util.ArrayList;

public class Polygon {

    private ArrayList<Integer> vertexIndices;
    private ArrayList<Integer> textureVertexIndices;
    private ArrayList<Integer> normalIndices;
    private int lineIndex;

    // Конструктор по умолчанию
    public Polygon() {
        vertexIndices = new ArrayList<>();
        textureVertexIndices = new ArrayList<>();
        normalIndices = new ArrayList<>();
    }

    // Конструктор с параметрами
    public Polygon(ArrayList<Integer> vertexIndices, ArrayList<Integer> textureVertexIndices, ArrayList<Integer> normalIndices) {
        setVertexIndices(vertexIndices);
        setTextureVertexIndices(textureVertexIndices);
        setNormalIndices(normalIndices); // Используем метод установки для нормалей
    }

    public void setVertexIndices(ArrayList<Integer> vertexIndices) {
        if (vertexIndices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь хотя бы 3 вершины.");
        }
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(ArrayList<Integer> textureVertexIndices) {
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(ArrayList<Integer> normalIndices) { // Новый метод установки нормалей
        this.normalIndices = normalIndices;
    }

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
        validateIndices(vertexIndices, verticesSize, "vertex");
        validateIndices(textureVertexIndices, textureVerticesSize, "texture vertex");
        validateIndices(normalIndices, normalsSize, "normal");
    }

    private void validateIndices(ArrayList<Integer> indices, int size, String type) { // Вынесена общая проверка
        for (int i = 0; i < indices.size(); i++) {
            int index = indices.get(i);
            if (index >= size || index < 0) {
                throw new FaceWordException(type, lineIndex, i + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "vertexIndices=" + vertexIndices +
                ", textureVertexIndices=" + textureVertexIndices +
                ", normalIndices=" + normalIndices +
                '}';
    }

    // Методы для добавления индексов
    public void addVertexIndex(int vertexIndex) {
        validateNonNegativeIndex(vertexIndex, "vertex");
        vertexIndices.add(vertexIndex);
    }

    public void addTextureVertexIndex(int textureVertexIndex) {
        validateNonNegativeIndex(textureVertexIndex, "texture");
        textureVertexIndices.add(textureVertexIndex);
    }

    public void addNormalIndex(int normalIndex) {
        validateNonNegativeIndex(normalIndex, "normal");
        normalIndices.add(normalIndex);
    }

    private void validateNonNegativeIndex(int index, String type) { // Общая проверка на отрицательные индексы
        if (index < 0) {
            throw new IllegalArgumentException("Индекс " + type + " не может быть отрицательным.");
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