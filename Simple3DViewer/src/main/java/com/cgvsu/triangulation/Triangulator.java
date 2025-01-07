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
                throw new RuntimeException("Не найдено ушко. Полигон может быть невыпуклым.");
            }
        }

        return triangles;
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
        Vector3f ab = Vector3f.subtract(b, a); // Получаем вектор ab
        Vector3f ac = Vector3f.subtract(c, a); // Получаем вектор ac

        // Векторное произведение ab и ac (в данном случае учитываем только Z-компонент)
        float crossProductZ = ab.getX() * ac.getY() - ab.getY() * ac.getX();

        // Если результат больше 0, угол является выпуклым
        return crossProductZ > 0;
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