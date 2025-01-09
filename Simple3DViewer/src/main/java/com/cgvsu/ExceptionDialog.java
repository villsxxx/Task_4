package com.cgvsu;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ExceptionDialog {
    public static void showError(Operation operation, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(operation == Operation.READING ? "Ошибка чтения" : "Ошибка записи");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public enum Operation {
        READING,
        WRITING
    }
}
