package com.cgvsu.triangulation;

import com.cgvsu.model.Polygon;
import com.cgvsu.model.Model;
import com.cgvsu.math.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class Triangulator {

    public List<Polygon> triangulate(Polygon polygon, Model model) {
        List<Polygon> triangles = new ArrayList<>();

        // Получаем вершины из полигона
        List<Vector3f> vertices = getVerticesFromPolygon(polygon, model);

        if (vertices.size() < 3) {
            throw new IllegalArgumentException("Полигон должен иметь хотя бы 3 вершины для триангуляции");
        }

        List<Vector3f> remainingVertices = new ArrayList<>(vertices);
        List<Integer> remainingIndices = new ArrayList<>(polygon.getVertexIndices());

        while (remainingVertices.size() >= 3) {
            boolean foundEar = false;

            for (int i = 0; i < remainingVertices.size(); i++) {
                int prevIndex = (i - 1 + remainingVertices.size()) % remainingVertices.size();
                int nextIndex = (i + 1) % remainingVertices.size();

                Vector3f a = remainingVertices.get(prevIndex);
                Vector3f b = remainingVertices.get(i);
                Vector3f c = remainingVertices.get(nextIndex);

                if (isEar(a, b, c, remainingVertices)) {
                    Polygon triangle = new Polygon();
                    int finalI = i;
                    triangle.setVertexIndices(new ArrayList<Integer>() {{
                        add(remainingIndices.get(prevIndex));
                        add(remainingIndices.get(finalI));
                        add(remainingIndices.get(nextIndex));
                    }});

                    triangles.add(triangle); // Добавляем треугольник в общий список
                    remainingVertices.remove(i); // Удаляем текущую вершину 'b'
                    remainingIndices.remove(i); // Также удаляем соответствующий индекс
                    foundEar = true;
                    break; // Возвращаемся в цикл, поскольку изменился размер remainingVertices
                }
            }

            if (!foundEar) {
                // Попробуем разбить полигон на меньшие участки
                if (!trySplitPolygon(remainingVertices, remainingIndices, triangles)) {
                    throw new RuntimeException("Не удалось разбить полигон. Полигон может быть невыпуклым.");
                }
            }
        }

        return triangles;
    }

    private boolean trySplitPolygon(List<Vector3f> remainingVertices, List<Integer> remainingIndices, List<Polygon> triangles) {
        // Ищем возможность добавить диагональ между двумя непоследовательными вершинами
        for (int i = 0; i < remainingVertices.size(); i++) {
            for (int j = i + 2; j < remainingVertices.size(); j++) {
                if (j != (i + 1) % remainingVertices.size() && canAddDiagonal(remainingVertices, i, j)) {
                    // Создаем новый треугольник и добавляем его в список
                    Polygon triangle = new Polygon();
                    int finalI = i;
                    int finalJ = j;
                    triangle.setVertexIndices(new ArrayList<Integer>() {{
                        add(remainingIndices.get(finalI));
                        add(remainingIndices.get((finalI + 1) % remainingVertices.size()));
                        add(remainingIndices.get(finalJ));
                    }});
                    triangles.add(triangle);

                    // Удаляем одну из вершин, чтобы уменьшить полигон
                    remainingVertices.remove(j);
                    remainingIndices.remove(j);
                    return true; // Успех, мы смогли разбить полигон
                }
            }
        }

        return false; // Не удалось разбить полигон
    }

    private boolean canAddDiagonal(List<Vector3f> vertices, int indexA, int indexB) {
        Vector3f a = vertices.get(indexA);
        Vector3f b = vertices.get(indexB);

        for (int i = 0; i < vertices.size(); i++) {
            int nextIndex = (i + 1) % vertices.size();

            // Пропускаем рёбра, к которым принадлежит одна из вершин диагонали
            if (i == indexA || i == nextIndex || i == indexB) continue;

            Vector3f c = vertices.get(i);
            Vector3f d = vertices.get(nextIndex);

            if (linesIntersect(a, b, c, d)) {
                return false; // Диагональ пересекает другое ребро
            }
        }

        return true; // Диагональ безопасна
    }

    private boolean linesIntersect(Vector3f p1, Vector3f p2, Vector3f q1, Vector3f q2) {
        float d1 = direction(q1, q2, p1);
        float d2 = direction(q1, q2, p2);
        float d3 = direction(p1, p2, q1);
        float d4 = direction(p1, p2, q2);

        // Если p1 и p2 находятся по разные стороны от линии q1q2
        if (d1 * d2 < 0 && d3 * d4 < 0) {
            return true;
        }
        // Коллинеарные случаи могут быть обработаны отдельно при необходимости
        return false;
    }

    private float direction(Vector3f pi, Vector3f pj, Vector3f pk) {
        return (pk.getY() - pi.getY()) * (pj.getX() - pi.getX()) - (pk.getX() - pi.getX()) * (pj.getY() - pi.getY());
    }



    private List<Vector3f> getVerticesFromPolygon(Polygon polygon, Model model) {
        List<Vector3f> vertices = new ArrayList<>();
        for (Integer index : polygon.getVertexIndices()) {
            vertices.add(model.getVertices().get(index)); // Получаем вершины модели по индексам полигона
        }
        return vertices;
    }

    private boolean isEar(Vector3f a, Vector3f b, Vector3f c, List<Vector3f> remainingVertices) {
        // 1. Проверка на выпуклость
        if (!isConvex(a, b, c)) {
            return false;
        }

        // 2. Проверка на наличие других вершин внутри треугольника
        for (Vector3f point : remainingVertices) {
            // Пропускаем, если это одна из вершин треугольника
            if (point.equals(a) || point.equals(b) || point.equals(c)) {
                continue;
            }

            // Проверяем, находится ли точка внутри треугольника
            if (pointInTriangle(point, a, b, c)) {
                return false; // Найдена точка внутри треугольника
            }
        }

        return true; // Треугольник - ухо
    }


    private boolean isConvex(Vector3f a, Vector3f b, Vector3f c) {
        // Векторы AB и AC
        Vector3f ab = Vector3f.subtract(b, a);
        Vector3f ac = Vector3f.subtract(c, a);

        // Векторное произведение AB x AC
        Vector3f crossProduct = Vector3f.cross(ab, ac);

        // Проверка знака Z-компоненты векторного произведения
        return crossProduct.getZ() > 0;
    }

    // Метод для проверки принадлежности точки треугольнику
    private boolean pointInTriangle(Vector3f p, Vector3f a, Vector3f b, Vector3f c) {
        float areaABC = triangleArea(a, b, c);
        float areaPAB = triangleArea(p, a, b);
        float areaPAC = triangleArea(p, a, c);
        float areaPBC = triangleArea(p, b, c);

        // Проверяем, равна ли сумма площадей треугольников PAB, PAC и PBC площади треугольника ABC
        return Math.abs(areaABC - (areaPAB + areaPAC + areaPBC)) < 1e-6; // Используем epsilon для учета ошибок округления
    }

    // Метод для вычисления площади треугольника по его вершинам
    private float triangleArea(Vector3f a, Vector3f b, Vector3f c) {
        return Math.abs((a.getX() * (b.getY() - c.getY()) +
                b.getX() * (c.getY() - a.getY()) +
                c.getX() * (a.getY() - b.getY())) / 2.0f);
    }
}