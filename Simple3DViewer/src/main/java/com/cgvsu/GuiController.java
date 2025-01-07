package com.cgvsu;

import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import com.cgvsu.math.*;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane mainPane;
    @FXML
    AnchorPane centerPane;

    @FXML
    private Canvas canvas;

    private Model mesh = null;

    private Camera camera = new Camera(
            new Vector3f(0, 00, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;


    private double lastMouseX; // Последняя X-координата мыши
    private double lastMouseY; // Последняя Y-координата мыши
    private static final float MOUSE_SENSITIVITY = 0.1f;


    @FXML
    private void initialize() {
        //centerPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        //centerPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        canvas.widthProperty().bind(centerPane.widthProperty()); // Вычитаем ширину боковой панели
        canvas.heightProperty().bind(centerPane.heightProperty());

        // Обработка нажатия мыши
        canvas.setOnMousePressed(event -> {
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        });

        // Обработка перемещения мыши
        canvas.setOnMouseDragged(event -> {
            double currentMouseX = event.getSceneX();
            double currentMouseY = event.getSceneY();

            // Вычисляем разницу
            double deltaX = currentMouseX - lastMouseX;
            double deltaY = currentMouseY - lastMouseY;

            // Двигаем камеру в зависимости от направления
            camera.movePosition(new Vector3f(
                    (float) -deltaX * MOUSE_SENSITIVITY,
                    (float) deltaY * MOUSE_SENSITIVITY,
                    0
            ));

            // Запоминаем текущие координаты мыши
            lastMouseX = currentMouseX;
            lastMouseY = currentMouseY;
        });
        canvas.setOnScroll(event -> {
            double deltaY = event.getDeltaY(); // Получаем направление прокрутки (вверх или вниз)

            // Двигаем камеру вперед или назад в зависимости от направления
            camera.movePosition(new Vector3f(0, 0, (float) -deltaY * MOUSE_SENSITIVITY));
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (mesh != null) {
                RenderEngine.render(canvas.getGraphicsContext2D(), camera, mesh, (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }
        Path fileName = Path.of(file.getAbsolutePath());
        try {
            String fileContent = Files.readString(fileName);
            mesh = ObjReader.read(fileContent);
            // todo: обработка ошибок
        } catch (IOException exception) {
        }
    }


    @FXML
    private void onSaveModelClick() {
        if (mesh == null) {
            System.out.println("No model to save.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj"));

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file != null) {
            try {
                ObjWriter writer = new ObjWriter();
                writer.write(mesh, file.getAbsolutePath());
                System.out.println("Model saved successfully!");
            } catch (Exception e) {
                System.err.println("Failed to save model: " + e.getMessage());
            }
        }
    }



    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }
}
