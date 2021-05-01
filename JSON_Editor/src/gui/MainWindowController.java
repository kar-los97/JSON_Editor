package gui;

import exceptions.JSONErrorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.WindowEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import validating.TextChangeChecking;
import values.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

public class MainWindowController {
    @FXML
    public MenuItem menuFileSave;
    @FXML
    public MenuItem menuFileOpen;
    @FXML
    public MenuItem menuFileSaveAs;
    @FXML
    public MenuItem menuEditFind;
    @FXML
    public MenuItem menuEditFindNext;
    @FXML
    public TextField tfFinding;
    @FXML
    private TreeView<String> treeJS;
    @FXML
    private CodeArea textAreaJSON;

    private Thread threadToTextCheck;
    private TextChangeChecking textChangeChecking;
    private JSONObject JSONobject;
    private File openedJSONFile;
    private String findingItem = "";
    private int index = -1;
    private boolean saved;


    @FXML
    private void initialize() {
        saved = false;
        textAreaJSON.setLineHighlighterOn(true);
        menuFileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN));
        menuFileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN));
        menuFileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN));
        menuEditFind.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN));
        menuEditFindNext.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        textAreaJSON.setParagraphGraphicFactory(LineNumberFactory.get(textAreaJSON));

        textChangeChecking = new TextChangeChecking(LocalDateTime.now(), "");
        threadToTextCheck = new Thread(textChangeChecking);
        threadToTextCheck.start();
        textAreaJSON.textProperty().addListener((observable, oldValue, newValue) -> {
            saved = false;
            textChangeChecking.setLastTimeTextChanged(LocalDateTime.now());
            textChangeChecking.setStringOfJSONFile(textAreaJSON.getText());
            if (!threadToTextCheck.isAlive()) {
                threadToTextCheck = new Thread(textChangeChecking);
                threadToTextCheck.start();
            }
        });
    }

    public void menuFileOpenOnAction(ActionEvent actionEvent) {
        File newFile = Files.getInstance().openJSON();
        if (newFile != null) {
            openedJSONFile = newFile;
            try {
                JSONobject = Files.getInstance().openJSONFromFile(openedJSONFile);
                textAreaJSON.replaceText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
                treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS, JSONobject);
                saved = true;
            } catch (JSONErrorException | IOException ex) {
                Alerts.getInstance().showAlert("JSON file openning ERROR", "Some error at openning JSON file", ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void menuFileCloseOnAction(ActionEvent actionEvent) {
        closeApp(null);
    }

    public void menuFileSaveAsOnAction(ActionEvent actionEvent) {
        openedJSONFile = Files.getInstance().saveJSONAs(textAreaJSON.getText());
        saved = true;
    }

    public void menuFileSaveOnAction(ActionEvent actionEvent) {
        openedJSONFile = Files.getInstance().saveJSON(openedJSONFile, textAreaJSON.getText());
        saved = true;
    }

    public void closeApp(WindowEvent event) {
        loadJsonToTree();
        if (JSONobject != null || !textAreaJSON.getText().isEmpty()) {
            String result = Alerts.getInstance().showClosingDialog();
            switch (result) {
                case "yes":
                    Main.stage.close();
                    break;
                case "no":
                    File newFile = Files.getInstance().saveJSONAs(textAreaJSON.getText());
                    if (newFile != null) {
                        openedJSONFile = newFile;
                        Main.stage.close();
                    } else {
                        event.consume();
                    }
                    break;
                case "cancel":
                    event.consume();
            }
        }

    }

    public void btnLoadJsonToTreeOnAction(ActionEvent actionEvent) {
        loadJsonToTree();
    }

    public void loadJsonToTree() {
        try {
            JSONobject = TextAreaParser.getInstance().loadJSONFromTextArea(textAreaJSON.getText(), JSONobject);
            treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS, JSONobject);
            textAreaJSON.replaceText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
        } catch (JSONErrorException ex) {
            Alerts.getInstance().showAlert("ERROR at refreshing tree", "Some error at refreshing tree", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void menuItemEditFindOnAction(ActionEvent actionEvent) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Find");
        textInputDialog.setHeaderText("Find in JSON");
        textInputDialog.setContentText("What are you finding?");
        try {
            findingItem = textInputDialog.showAndWait().get();
        } catch (NoSuchElementException ex) {
            return;
        }
        tfFinding.setText(findingItem);
        index = 0;
        find();
    }

    public void menuItemEditFindNext(ActionEvent actionEvent) {
        find();
    }

    public void menuHelpAboutOnAction(ActionEvent actionEvent) {
        String text = "";
        text += "This application was made as Bachelor thesis.\n";
        text += "The Application is used to edit JSON files. \n";
        text += "You can open JSON file, edit JSON file and then sasve JSON file. \n";
        text += "Syntax of JSON files are validating in real-time. \n";
        text += "You can finding in JSON files, Menu-Edit-Find (Ctrl + F).\n";
        text += "You can finding next in JSON files, Menu-Edit-Find (Ctrl + Shift + F).\n";
        text += "Author: Karel Andres, © 2021, Fakulta Elektrotechniky a informatiky, Univerzita Pardubice\n";
        Alerts.getInstance().showAlert("About", "Something about application", text, Alert.AlertType.INFORMATION);
    }

    public void btnFindNextOnAction(ActionEvent actionEvent) {
        find();
    }

    public void btnFindOnAction(ActionEvent actionEvent) {
        findingItem = tfFinding.getText();
        index = 0;
        find();
    }

    private void find() {
        if (findingItem != "" && index != -1) {
            int newIndex = textAreaJSON.getText().indexOf(findingItem, index);
            if (newIndex != -1) {
                textAreaJSON.selectRange(newIndex, newIndex + findingItem.length());
                textAreaJSON.requestFollowCaret();
                index = newIndex + findingItem.length();
            } else if (index == 0) {
                Alerts.getInstance().showAlert("Nothing find", "Nothing find", "\"" + findingItem + "\"" + " wasn't find usage in file!", Alert.AlertType.CONFIRMATION);
            } else {
                Alerts.getInstance().showAlert("Nothing find", "Nothing find", "\"" + findingItem + "\"" + " wasn't find next usage in file!", Alert.AlertType.CONFIRMATION);
            }

        }
    }
}
