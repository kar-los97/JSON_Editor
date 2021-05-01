package gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {

    private static Alerts instance = null;

    public static Alerts getInstance() {
        if (instance == null) {
            instance = new Alerts();
        }
        return instance;
    }

    public Optional<ButtonType> showAlert(String title, String headerText, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public String showClosingDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Closing without saving");
        alert.setHeaderText("Are you sure you want to close the file without saving?");

        ButtonType buttonTypeYes = new ButtonType("Yes, close without save");
        ButtonType buttonTypeNo = new ButtonType("No, I want save file");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonTypeYes) {
                return "yes";
            } else if (result.get() == buttonTypeNo) {
                return "no";
            } else {
                return "cancel";
            }
        } else {
            return "cancel";
        }

    }
}
