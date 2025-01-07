package com.cgvsu.render_engine;

import java.util.ArrayList;

import com.cgvsu.TL.Texture;
import com.cgvsu.math.*;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon; // Импортируем Polygon
import static com.cgvsu.render_engine.GraphicConveyor.*;
import com.cgvsu.render_engine.PolygonRasterizer;


public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height,
            final Texture texture) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = Matrix4f.createIdentityMatrix();
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(modelMatrix);

        final int nPolygons = mesh.polygons.size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            Polygon polygon = mesh.polygons.get(polygonInd); // Получаем текущий полигон
            final int nVerticesInPolygon = polygon.getVertexIndices().size();

            ArrayList<Vector2f> resultPoints = new ArrayList<>();
            ArrayList<Vector2f> uvCoordinates = new ArrayList<>(); // Объявление и инициализация списка для UV-координат

            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                int vertexIndex = polygon.getVertexIndices().get(vertexInPolygonInd);
                Vector3f vertex = mesh.vertices.get(vertexIndex);
                Vector2f resultPoint = vertexToPoint(Vector3f.multiply(vertex, modelViewProjectionMatrix), width, height);
                resultPoints.add(resultPoint);

                // Получаем индекс текстурной координаты
                if (polygon.hasTexture()) { // Проверяем, есть ли текстура
                    int textureIndex = polygon.getTextureVertexIndices().get(vertexInPolygonInd);
                    Vector2f uv = mesh.getTextureVertices().get(textureIndex);
                    uvCoordinates.add(uv);
                } else {
                    uvCoordinates.add(new Vector2f(0, 0)); // Добавляем нулевую UV, если текстуры нет
                }
            }

            // Рисуем линии между вершинами полигона
            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());

            }
        }
    }
