package com.cgvsu;

import com.cgvsu.Scene.SceneManager;
import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cgvsu.math.*;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import static com.cgvsu.ExceptionDialog.throwExceptionWindow;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane mainPane;
    @FXML
    AnchorPane centerPane;

    @FXML
    private Canvas canvas;

    @FXML
    private CheckBox grid;

    @FXML
    private ListView<String> modelList;

    //private SceneManager sceneManager = new SceneManager();
    private Model selectedModel;
    private List<Float> modelCenters = new ArrayList<>();
    private float x = 0;
    private float spacing = 5.0f;

    private List<Model> models = new ArrayList<>();

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
        canvas.widthProperty().bind(centerPane.widthProperty());
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

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0,0,width,height);
            if(grid.isSelected()){

                drawGrid(gc,width,height);
            }
            gc.setStroke(Color.BLACK);
            //canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (models != null) {
                for (Model mesh: models) {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, models, (int) width, (int) height);
                }
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
            addToModelsList(fileName);
            Model newModel = ObjReader.read(fileContent);
            if (newModel == null) {
                throwExceptionWindow(ExceptionDialog.Operation.READING);
            }
            selectedModel = newModel;
            models.add(selectedModel);
//!!!!! что то сделать
            translateModel(newModel, x, 0, 0);

            if (models.isEmpty()) {
                newModel.selected = true;
                selectedModel = newModel;
            }
            models.add(newModel);
            modelCenters.add(x);
            x += newModel.xSize + spacing;
            modelList.getItems().add(file.getName());
            // todo: обработка ошибок
        } catch (IOException exception) {

        }
    }

    @FXML
    private void onSaveModelClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OBJ files (*.obj)", "*.obj"));

        File file = fileChooser.showSaveDialog((Stage) canvas.getScene().getWindow());
        if (file != null) {
            ObjWriter writer = new ObjWriter();
            writer.write(selectedModel, file.getAbsolutePath());
            //ObjWriter.write(selectedModel, file.getAbsolutePath());
        }
    }

     //простая сетка нужна другая
    private void drawGrid(GraphicsContext gc, double w,double h){
        gc.setStroke(Color.GREEN);
        gc.setLineWidth(0.5);
        double cellSize =50.0;
        for(double x=0;x<w;x+=cellSize){
            gc.strokeLine(x,0,x,h);
        }
        for(double y=0;y<w;y+=cellSize){
            gc.strokeLine(0,y,w,y);
        }
    }
/*
    @FXML
    private void updateModelList() {
        List<String> modelNames = new ArrayList<>();
        for (int i = 0; i < sceneManager.getModels().size(); i++) {

            modelNames.add("Model " + (i + 1)); // Названия моделей (можно заменить на более осмысленные)
        }
        modelList.getItems().setAll(modelNames); // Обновляем ListView
    }
    @FXML
    private void onModelSelected(){
        int selectedIndex = modelList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >=0){
            sceneManager.setActiveModel(selectedIndex);
        }
    }
    @FXML
    private void onRender() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight()); // Очищаем область рисования

        sceneManager.renderAllModels(gc, camera, (int) canvas.getWidth(), (int) canvas.getHeight());
    }*/


    /*
    !!!!!!!!!!!!!!!!!!!!!!!
    */
    private void translateModel(Model model, float tx, float ty, float tz) {
        for (com.cgvsu.math.Vector3f vertex : model.getVertices()) {
            // Преобразуем Vector3f в Vector4f, добавив однородную координату
            Vector4f vertex4f = new Vector4f(vertex.getX(), vertex.getY(), vertex.getZ(), 1);

            // Применяем трансляцию через AffineTransforms
            AffineTransforms.translate(vertex4f, tx, ty, tz);

            // Обновляем координаты в вершине модели
            //vertex.setX(vertex4f.getX());
            //vertex.setY(vertex4f.getY());
            //vertex.setZ(vertex4f.getZ());
        }
    }

    @FXML
    private void addToModelsList(Path fileName) {
        String name = fileName.toString();
        MenuButton modelButton = new MenuButton(name.split("\\\\")[name.split("\\\\").length - 1]);
        modelButton.setMinWidth(240);
        modelButton.setMaxWidth(240);
        CheckMenuItem polygonMeshShow = new CheckMenuItem("Полигональная сетка");
        polygonMeshShow.setSelected(true);
        CheckMenuItem textureShow = new CheckMenuItem("Текстура");
        textureShow.setSelected(false);
        CheckMenuItem lightingShow = new CheckMenuItem("Освещение");
        lightingShow.setSelected(false);
        RadioMenuItem pinCamera = new RadioMenuItem("Центрировать камеру");
        pinCamera.setSelected(models.isEmpty());
        int modelIndex = models.size();

        //pinCamera.setOnAction(actionEvent -> setCameraTarget(modelIndex));

        //camerasPinGroup.getToggles().add(camerasPinGroup.getToggles().size(), pinCamera);
        modelButton.getItems().add(pinCamera);

        modelButton.getItems().add(polygonMeshShow);
        modelButton.getItems().add(textureShow);
        modelButton.getItems().add(lightingShow);

        //modelsMenuBox.getChildren().add(modelButton);
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
    @FXML
    public void toggleGrid(ActionEvent actionEvent) {
    }
}
