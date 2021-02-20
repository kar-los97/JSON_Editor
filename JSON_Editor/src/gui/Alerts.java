package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    private static Alerts instance = null;

    public static Alerts getInstance() {
        if(instance==null){
            instance = new Alerts();
        }
        return instance;
    }

    public static Optional<ButtonType> showAlert(String title, String headerText, String contentText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }
}
