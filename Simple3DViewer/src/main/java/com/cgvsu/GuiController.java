package com.cgvsu;

import com.cgvsu.VertexDelete.Eraser;
import com.cgvsu.VertexDelete.EraserV2;
import com.cgvsu.obj_writer.ObjWriter;
import com.cgvsu.reader.Reader;
import com.cgvsu.render_engine.RenderEngine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.util.function.Function;

import com.cgvsu.math.*;

import com.cgvsu.model.Model;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.render_engine.Camera;

import static com.cgvsu.ExceptionDialog.throwExceptionWindow;
import static com.cgvsu.render_engine.RenderEngine.renderModel;

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
    private int selectedModelIndex;
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
    private List<String> modelNames = new ArrayList<>();
    @FXML
    private TextField textScale;
    @FXML
    private TextField textRotate;
    @FXML
    private TextField textTranslate;
    @FXML
    private CheckBox boxSaveScale;
    @FXML
    private CheckBox boxSaveRotate;
    @FXML
    private CheckBox boxSaveTranslate;
    @FXML
    private Button buttonDeleteModel;
    @FXML
    private TextField textVertexDel; // Поле для ввода индексов
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

        textScale.setText("1, 1, 1");
        textRotate.setText("0x, 0y, 0z");
        textTranslate.setText("0 0 0");


        MultipleSelectionModel<String> modelsSelectionModel = modelList.getSelectionModel();
        //слушатель для отслеживания изменений
        modelsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){
                String item = modelsSelectionModel.getSelectedItem();
                if(!Objects.isNull(item)) {
                    selectedModelIndex = modelNames.indexOf(item);
                    selectedModel = models.get(selectedModelIndex);
                }
            }
        });
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
            Model newModel = ObjReader.read(fileContent);
            if (newModel == null) {
                throwExceptionWindow(ExceptionDialog.Operation.READING);
            }
            models.add(newModel);
            selectedModel = newModel;
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
    @FXML
    private void scale(ActionEvent event) {
        if(Objects.isNull(selectedModel)){
            return;
        }
        String s = textScale.getText();
        String[] coefficient = Reader.readNumbersInLine(s, "1", 3);
        selectedModel.getTransformation().setScale(AffineTransforms.scale(Float.parseFloat(coefficient[0]),
                Float.parseFloat(coefficient[1]), Float.parseFloat(coefficient[2])));
        if (boxSaveScale.isSelected()) {
            selectedModel.saveScale();
        }

    }
    @FXML
    private void translate(ActionEvent event) {
        if(Objects.isNull(selectedModel)){
            return;
        }
        String s = textTranslate.getText();
        String[] coefficient = Reader.readNumbersInLine(s, "0", 3);
        selectedModel.getTransformation().setTranslation(AffineTransforms.translate(Float.parseFloat(coefficient[0]),
                Float.parseFloat(coefficient[1]), Float.parseFloat(coefficient[2])));
        if (boxSaveTranslate.isSelected()) {
            selectedModel.saveTranslation();
        }
    }
    @FXML
    private void rotate(ActionEvent event) {
        if(Objects.isNull(selectedModel)){
            return;
        }
        String s = textRotate.getText();
        String[] coefficient = Reader.readNumbersInLineWithXYZ(s, "0");
        Function<Double, Double> fromDegreeToRad = (a) -> (a*Math.PI)/180;
        double rotX = fromDegreeToRad.apply(Double.parseDouble(coefficient[0]));
        double rotY = fromDegreeToRad.apply(Double.parseDouble(coefficient[1]));
        double rotZ = fromDegreeToRad.apply(Double.parseDouble(coefficient[2]));
        selectedModel.getTransformation().setRotation(
                AffineTransforms.rotateX(Math.cos(rotX), Math.sin(rotX)).getMultiplicationWith(
                        AffineTransforms.rotateY(Math.cos(rotY), Math.sin(rotY)).getMultiplicationWith(
                                AffineTransforms.rotateZ(Math.cos(rotZ), Math.sin(rotZ))
                        )));
        if (boxSaveRotate.isSelected()) {
            selectedModel.saveRotation();
        }
    }
    @FXML
    private void deleteModel(){
        if(Objects.isNull(selectedModel)){
            return;
        }
        models.remove(selectedModelIndex);
        modelNames.remove(selectedModelIndex);
        if(models.isEmpty()){
            selectedModel = null;
        } else{
            selectedModel = models.get(0);
            selectedModelIndex = 0;
        }
        selectedModelIndex = 0;

        modelList.getItems().remove(selectedModelIndex);
    }
    @FXML
    private void deleteVertices() {
        try {
            // Считываем индексы вершин из текстового поля
            String input = textVertexDel.getText();
            // Преобразуем строку в список индексов
            List<Integer> indicesToDelete = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            Model uptadeModel = new Model();

            // Удаляем вершины с использованием Eraser
            uptadeModel = EraserV2.vertexDelete(
                    selectedModel,                   // Исходная модель
                    indicesToDelete,         // Индексы для удаления
                    true,                    // Изменяем текущую модель (не создаем новую)
                    false,                  // Не удаляем висячие нормали
                    false,                   // Не удаляем висячие текстуры
                    true                   // Удаляем полигоны с менее чем 3 вершинами
            );
           // RenderEngine.render(canvas.getGraphicsContext2D(), camera,  );
            Model newModel = ObjReader.read(uptadeModel.toString());
            // Очистка текстового поля после успешного выполнения
            textVertexDel.clear();
        } catch (Exception e) {
            System.out.println("Ошибка при удалении вершин: " + e.getMessage());
        }
    }


}

