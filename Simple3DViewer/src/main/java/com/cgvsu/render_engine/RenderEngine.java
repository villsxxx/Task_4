package com.cgvsu.render_engine;

import java.util.ArrayList;
import java.util.List;

import com.cgvsu.math.*;
import javafx.scene.canvas.GraphicsContext;
import com.cgvsu.model.Model;
import javafx.scene.paint.Color;

import javax.vecmath.Point2f;

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

        Matrix4f modelMatrix =  model.getModelMatrix();
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

}