package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.List;

import com.cgvsu.TL.Texture;
import com.cgvsu.math.*;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.vecmath.Point2f;
import com.cgvsu.TL.ImageToText;

import static com.cgvsu.render_engine.GraphicConveyor.*;


public class RenderEngine {

    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final List<Model> models,
            final int width,
            final int height) {


        for (Model mesh : models) {
            if (mesh.viewMesh) renderModel(graphicsContext, camera, mesh, width, height);
        }
    }

    private static class PointVertexModel {

        public PointVertexModel(Point2f point, int vertexIndex, Model model) {
            this.point = new Point2f((float) Math.floor(point.x), (float) Math.floor(point.y));
            this.vertexIndex = vertexIndex;
            this.model = model;
        }

        public PointVertexModel(int vertexIndex) {
            this.point = new Point2f(0, 0);
            this.vertexIndex = vertexIndex;
            this.model = new Model();
        }

        public Point2f point;
        public int vertexIndex;

        public Model model;
    }

    private static class SelectedPolygon {
        public Model model;
        public int selPoly = -2;

        public SelectedPolygon(int poly, Model model) {
            this.selPoly = poly;
            this.model = model;
        }

        public SelectedPolygon() {
            this.selPoly = -2;
            this.model = new Model();
        }
    }

    private static PointVertexModel selectedPVM = new PointVertexModel(-2);
    private static SelectedPolygon selectedPoly = new SelectedPolygon();


    public static void renderModel(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model model,
            final int width,
            final int height) {

        Matrix4f modelMatrix = model.getModelMatrix();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = Matrix4f.createIdentityMatrix();
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(modelMatrix);

        final int nPolygons = model.polygons.size();

        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = model.polygons.get(polygonInd).getVertexIndices().size();

            ArrayList<Vector2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = model.vertices.get(model.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Vector2f resultPoint = vertexToPoint(Vector3f.multiply(vertex, modelViewProjectionMatrix), width, height);
                resultPoints.add(resultPoint);
            }

            graphicsContext.setStroke((model.selected) ? Color.BLACK : Color.GRAY);

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                if (model.selected) {
                    graphicsContext.setStroke(
                            (selectedPVM.vertexIndex > -2
                                    && (model.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd) == selectedPVM.vertexIndex
                                    || model.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd - 1) == selectedPVM.vertexIndex))
                                    ? Color.RED
                                    : graphicsContext.getStroke()
                    );
                    if (selectedPoly.selPoly > -2) {
                        graphicsContext.setStroke(
                                (polygonInd == selectedPoly.selPoly)
                                        ? Color.BLUE
                                        : graphicsContext.getStroke()
                        );
                        graphicsContext.setLineWidth((polygonInd == selectedPoly.selPoly)
                                ? 2
                                : 1);
                    }
                }
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0) {
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
            }
        }
    }

    public static void renderTexture(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model model,
            final int width,
            final int height) {

        // Получаем матрицы для обработки трансформаций
        Matrix4f modelMatrix = model.getModelMatrix();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        // Создание модели видеопрезентации
        Matrix4f modelViewProjectionMatrix = Matrix4f.createIdentityMatrix();
        modelViewProjectionMatrix.multiply(projectionMatrix);
        modelViewProjectionMatrix.multiply(viewMatrix);
        modelViewProjectionMatrix.multiply(modelMatrix);

        final int nPolygons = model.polygons.size();

        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = model.polygons.get(polygonInd).getVertexIndices().size();
            ArrayList<Vector2f> resultPoints = new ArrayList<>();

            // Сохраняем UV-координаты
            Vector2f[] uvCoords = new Vector2f[nVerticesInPolygon];

            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                int vertexIndex = model.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd);
                Vector3f vertex = model.vertices.get(vertexIndex);
                Vector2f uv = model.textureVertices.get(vertexIndex);

                // Преобразуем 3D-координату в 2D
                Vector2f resultPoint = vertexToPoint(Vector3f.multiply(vertex, modelViewProjectionMatrix), width, height);
                resultPoints.add(resultPoint);
                uvCoords[vertexInPolygonInd] = uv; // Сохраняем UV-координаты
            }

            // Отрисовка текстуры
            if (model.textureVertices != null) {
                Texture texture = new Texture();
                Image image = texture.getImage();
                for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                    Vector2f uv = uvCoords[vertexInPolygonInd];

                    // Преобразуем UV-координаты в пиксели
                    double textureX = uv.getX() * image.getWidth();
                    double textureY = uv.getY() * image.getHeight();

                    // Рисуем текстуру по соответствующим координатам
                    int[] rgb = new int[3];
                    ImageToText.applyTexture(uv, model, rgb);
                    graphicsContext.setFill(Color.rgb(rgb[0], rgb[1], rgb[2]));
                    graphicsContext.fillRect((int) textureX, (int) textureY, 1, 1); // Размер рисуемой области
                }
            }
        }
    }
}