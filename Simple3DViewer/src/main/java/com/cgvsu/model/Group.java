package com.cgvsu.model;

import java.util.ArrayList;

public class Group {
    private final String name;
    private final ArrayList<Polygon> polygons = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }

    public boolean removePolygon(Polygon polygon) {
        return polygons.remove(polygon);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Polygon> getPolygons() {
        return polygons;
    }

    public int getPolygonsSize() {
        return polygons.size();
    }

    public int getTotalVertexCount() {
        int count = 0;
        for (Polygon polygon : polygons) {
            count += polygon.getVertexIndices().size();
        }
        return count;
    }

    public int getTotalNormalsCount() {
        int count = 0;
        for (Polygon polygon : polygons) {
            count += polygon.getNormalIndices().size();
        }
        return count;
    }
}