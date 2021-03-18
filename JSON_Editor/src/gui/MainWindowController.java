package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import validating.TextChangeChecking;
import values.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class MainWindowController {
    private Thread threadToTextCheck;
    private TextChangeChecking textChangeChecking;
    private JSONObject JSONobject;
    private File openedJSONFile;
    @FXML
    private TreeView<String> treeJS;
    @FXML
    private TextArea textAreaJSON;

    @FXML
    private void initialize() {
        //textChangeChecking = new TextChangeChecking(LocalDateTime.now(),"");
        //threadToTextCheck = new Thread(textChangeChecking);
        //threadToTextCheck.start();
        /*textAreaJSON.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!threadToTextCheck.isAlive()){
                textChangeChecking = new TextChangeChecking(LocalDateTime.now(),textAreaJSON.getText());
                threadToTextCheck = new Thread(textChangeChecking);
                threadToTextCheck.start();
            }
            textChangeChecking.setLastTimeTextChanged(LocalDateTime.now());
            textChangeChecking.setStringOfJSONFile(textAreaJSON.getText());
        });*/
    }

    public void menuFileOpenOnAction(ActionEvent actionEvent){
        openedJSONFile = Files.getInstance().openJSON();
        JSONobject = Files.getInstance().openJSONFromFile(openedJSONFile);
        treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS,JSONobject);
        textAreaJSON.setText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
    }

    public void menuFileCloseOnAction(ActionEvent actionEvent){
        closeApp();
    }

    public void menuFileSaveAsOnAction(ActionEvent actionEvent) {
        Files.getInstance().saveJSONAs(textAreaJSON.getText());
    }

    public void menuFileSaveOnAction(ActionEvent actionEvent){
        Files.getInstance().saveJSON(openedJSONFile,textAreaJSON.getText());
    }

    private void closeApp() {
        Optional<ButtonType> optionalBtn = Alerts.getInstance().showAlert("Closing","Are you sure that you want close the file?","Do you want save the file before close it?", Alert.AlertType.CONFIRMATION);
        if(optionalBtn.get().equals(ButtonType.OK)){
            Files.getInstance().saveJSONAs(textAreaJSON.getText());
        }else{
            Main.stage.close();
        }
    }

    public void btnRefreshTreeViewOnAction(ActionEvent actionEvent) {
        JSONobject = TextAreaParser.getInstance().loadJSONFromTextArea(textAreaJSON.getText(),JSONobject);
        treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS,JSONobject);
        textAreaJSON.setText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
    }
}
