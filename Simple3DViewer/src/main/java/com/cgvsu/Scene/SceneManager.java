package com.cgvsu.Scene;

import com.cgvsu.math.AffineTransforms;
import com.cgvsu.math.Matrix4f;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    /*
    private List<Model> models = new ArrayList<>();
    private int activeModelIndex = -1;

    public void addModel(Model model) {
        models.add(model);
    }

    public void removeModel(int index) {
        if (index >= 0 && index < models.size()) {
            models.remove(index);
        }
    }

    public Model getActiveModel() {
        return activeModelIndex >= 0 ? models.get(activeModelIndex) : null;
    }

    public void setActiveModel(int index) {
        if (index >= 0 && index < models.size()) {
            activeModelIndex = index;
        }
    }

    public List<Model> getModels() {
        return models;
    }
    public void renderAllModels(GraphicsContext graphicsContext, Camera camera, int width, int height) {
        for (int i = 0; i < models.size(); i++) {
            Model model = models.get(i);

            // Применяем смещение для каждой модели
            Matrix4f translationMatrix = AffineTransforms.translate(i * 5.0f, 0, 0); // Смещаем модели по оси X
            model.setModelMatrix(translationMatrix);

            // Отрисовываем модель
            RenderEngine.renderAllModels(graphicsContext, camera, model, width, height);
        }
    }*/
}
