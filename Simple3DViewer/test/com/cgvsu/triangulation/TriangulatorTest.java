package com.cgvsu.triangulation;

import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.math.Vector3f;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TriangulatorTest {

    // Тест для триангуляции простого квадрата
    @Test
    public void testTriangulateSimplePolygon() {
        Model model = new Model();
        // Добавляем вершины квадрата в модель
        model.addVertex(new Vector3f(0, 0, 0));
        model.addVertex(new Vector3f(1, 0, 0));
        model.addVertex(new Vector3f(1, 1, 0));
        model.addVertex(new Vector3f(0, 1, 0));

        Polygon polygon = new Polygon();
        // Устанавливаем индексы вершин для квадрата
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));

        Triangulator triangulator = new Triangulator();
        // Выполняем триангуляцию и получаем список треугольников
        List<Polygon> triangles = triangulator.triangulate(polygon, model);

        // Проверяем, что был получен 2 треугольника
        assertEquals(2, triangles.size());
    }

    // Тест для триангуляции выпуклого многоугольника
    @Test
    public void testTriangulateConvexPolygon() {
        Model model = new Model();
        // Добавляем вершины выпуклого многоугольника в модель
        model.addVertex(new Vector3f(0, 0, 0));
        model.addVertex(new Vector3f(2, 0, 0));
        model.addVertex(new Vector3f(3, 2, 0));
        model.addVertex(new Vector3f(1, 3, 0));
        model.addVertex(new Vector3f(-1, 2, 0));
        model.addVertex(new Vector3f(0, 1, 0));

        Polygon polygon = new Polygon();
        // Устанавливаем индексы вершин для выпуклого многоугольника
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5)));

        Triangulator triangulator = new Triangulator();
        // Выполняем триангуляцию и получаем список треугольников
        List<Polygon> triangles = triangulator.triangulate(polygon, model);

        // Проверяем, что хотя бы один треугольник был создан
        assertTrue(triangles.size() > 0);
    }

    @Test
    public void testTriangulateInvalidPolygon() {
        Model model = new Model();

        // Создаем полигон с недостаточным количеством вершин (например, 2 вершины)
        Polygon polygon = new Polygon();
        // Устанавливаем индексы вершин (две вершины - это недопустимо для триангуляции)
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1)));

        Triangulator triangulator = new Triangulator();

        // Проверяем, что выбрасывается исключение при попытке триангулировать
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            triangulator.triangulate(polygon, model);
        });

        // Ожидаем, что сообщение исключения будет содержать информацию о недостаточном количестве вершин
        String expectedMessage = "Полигон должен иметь хотя бы 3 вершины для триангуляции";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    // Тест для триангуляции сложного многоугольника
    @Test
    public void testTriangulateComplexPolygon() {
        Model model = new Model();
        // Добавляем вершины сложного многоугольника в модель
        model.addVertex(new Vector3f(0, 0, 0));
        model.addVertex(new Vector3f(2, 0, 0));
        model.addVertex(new Vector3f(1, 1, 0));
        model.addVertex(new Vector3f(3, 3, 0));
        model.addVertex(new Vector3f(0, 2, 0));

        Polygon polygon = new Polygon();
        // Устанавливаем индексы вершин для сложного многоугольника
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)));

        Triangulator triangulator = new Triangulator();
        // Выполняем триангуляцию и получаем список треугольников
        List<Polygon> triangles = triangulator.triangulate(polygon, model);

        // Проверяем, что хотя бы один треугольник был создан
        assertTrue(triangles.size() > 0);
    }

    // Тест для триангуляции загруженной модели
    @Test
    public void testTriangulateLoadedModel() throws Exception {
        // Путь к файлу вашей модели (замените на действительный путь)
        Path path = Paths.get("Simple3DViewer\\test\\com\\cgvsu\\triangulation\\caracal_cube.obj");

        // Загрузка модели из файла
        Model model = loadModelFromFile(path.toString()); // Используем path.toString()

        // Создаем полигон, используя загруженную модель
        Polygon polygon = createPolygonFromModel(model);

        Triangulator triangulator = new Triangulator();

        // Выполняем триангуляцию и получаем список треугольников
        List<Polygon> triangles = triangulator.triangulate(polygon, model);

        // Проверяем, что хотя бы один треугольник был создан
        assertTrue(triangles.size() > 0);
    }

    // Метод загрузки модели из файла
    private Model loadModelFromFile(String filePath) throws Exception {
        Model model = new Model();

        // Чтение файла и добавление вершин в модель
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        for (String line : lines) {
            if (line.startsWith("v ")) { // Предполагается, что вершины начинаются с "v"
                String[] parts = line.split("\\s+");
                Vector3f vertex = new Vector3f(
                        Float.parseFloat(parts[1]),
                        Float.parseFloat(parts[2]),
                        Float.parseFloat(parts[3])
                );
                model.addVertex(vertex);
            }

        }

        return model;
    }

    // Метод создания полигона из модели
    private Polygon createPolygonFromModel(Model model) {
        Polygon polygon = new Polygon();

        // Устанавливаем индексы всех вершин в полигоне
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        for (int i = 0; i < model.getVerticesSize(); i++) {
            vertexIndices.add(i);
        }

        polygon.setVertexIndices(vertexIndices);
        return polygon;
    }
}