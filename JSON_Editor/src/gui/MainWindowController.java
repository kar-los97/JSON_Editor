package gui;

import exceptions.JSONErrorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import lexing.*;
import parsing.*;
import reading.*;
import tokens.Token;
import validating.TextChangeChecking;
import values.*;
import converting.*;
import writing.*;
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
        textChangeChecking = new TextChangeChecking(LocalDateTime.now(),"");
        threadToTextCheck = new Thread(textChangeChecking);
        threadToTextCheck.start();
        textAreaJSON.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!threadToTextCheck.isAlive()){
                textChangeChecking = new TextChangeChecking(LocalDateTime.now(),textAreaJSON.getText());
                threadToTextCheck = new Thread(textChangeChecking);
                threadToTextCheck.start();
            }
            textChangeChecking.setLastTimeTextChanged(LocalDateTime.now());
            textChangeChecking.setStringOfJSONFile(textAreaJSON.getText());
        });
    }

    private static Optional<ButtonType> showAlert(String title, String headerText, String contentText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert.showAndWait();
    }

    public void menuFileOpenOnAction(ActionEvent actionEvent) throws IOException, JSONErrorException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        openedJSONFile = chooser.showOpenDialog(Main.stage);
        JSONobject = loadJSONFromFile(openedJSONFile);
        loadJSONToTreeView();
    }

    private void loadJSONToTreeView() {
        treeJS.setRoot(new TreeItem<>());
        TreeItem<String> rootItem = new TreeItem<> ("{");
        rootItem.setExpanded(true);

        addTreeItems(JSONobject, rootItem, true);

        rootItem.getChildren().add(new TreeItem<>("}"));
        treeJS.setRoot(rootItem);
        IJSONConverter JSONConverter = new JSONConverter();
        try {
            textAreaJSON.setText(JSONConverter.convertJSON(JSONobject));
        }catch (JSONErrorException ex){
            showAlert("JSON converting error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void addTreeItems(JSONValue valueToAdd, TreeItem<String> treeItem, boolean printName) {
        for (JSONValue value : (List<JSONValue>) valueToAdd.getValue()) {
            addTreeItem(treeItem, value, printName);
        }
    }

    private void addTreeItem(TreeItem<String> rootItem, JSONValue valueToAdd, boolean printName) {
        TreeItem<String> trItem;

        if (valueToAdd instanceof JSONArray) {
            addJsonArray(rootItem, valueToAdd,printName);
        } else if (valueToAdd instanceof JSONObject) {
            addJsonObject(rootItem, valueToAdd,printName);
        } else if (printName) {
            trItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": " + valueToAdd.toString());
            trItem.setExpanded(true);
            rootItem.getChildren().add(trItem);
        } else {
            trItem = new TreeItem<>("" + valueToAdd.toString());
            trItem.setExpanded(true);
            rootItem.getChildren().add(trItem);
        }
    }

    private void addJsonArray(TreeItem<String> rootItem, JSONValue valueToAdd,boolean printName) {
        TreeItem<String> treeItem;
        if(printName){
            treeItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": [");
        }else{
            treeItem = new TreeItem<>("[");
        }
        treeItem.setExpanded(true);

        addTreeItems(valueToAdd, treeItem, false);

        rootItem.getChildren().add(treeItem);
        rootItem.getChildren().add(new TreeItem<>("]"));

    }


    private void addJsonObject(TreeItem<String> rootItem, JSONValue valueToAdd,boolean printName) {
        TreeItem<String> treeItem;
        if(printName){
            treeItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": {");
        }else{
            treeItem = new TreeItem<>("{");
        }
        treeItem.setExpanded(true);

        addTreeItems(valueToAdd, treeItem, true);

        rootItem.getChildren().add(treeItem);
        rootItem.getChildren().add(new TreeItem<>("}"));
    }

    private JSONObject loadJSONFromFile(File openenedJSONFile) throws IOException, JSONErrorException {
        IJSONReader jsReader = new JSONReader();
        String jsonAsString = jsReader.readJSONFromFile(openedJSONFile);
        List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonAsString);
        IJSONParser jsParser = new JSONParser();
        Queue<Token> tokens = jsParser.createTokensFromLexems(lexems);
        JSONObject jsonObject = jsParser.parseJSObject(tokens,null);
        return jsonObject;
    }

    private JSONObject loadJSONFromTextArea(){
        try {
            List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(textAreaJSON.getText());
            IJSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.parseJSObject(jsonParser.createTokensFromLexems(lexems), "");
            return jsonObject;
        }catch (JSONErrorException ex){
            showAlert("JSON loading Error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
            return JSONobject;
        }
    }

    public void menuFileCloseOnAction(ActionEvent actionEvent) throws IOException, JSONErrorException {
        closeApp();
    }

    private void closeApp() throws IOException, JSONErrorException {
        Optional<ButtonType> optionalBtn = showAlert("Closing","Are you sure that you want close the file?","Do you want save the file before close it?", Alert.AlertType.CONFIRMATION);
        if(optionalBtn.get().equals(ButtonType.OK)){
            saveJSONAs();
        }else{
            Main.stage.close();
        }
    }

    private void saveJSON(File fileToSave){
        IJSONParser jsonParser = new JSONParser();
        Queue<Token> tokens = jsonParser.createTokensFromLexems(Lexer.getInstance().createLexemsFromString(textAreaJSON.getText()));
        try {
            JSONobject = jsonParser.parseJSObject(tokens, null);
            IJSONWriter JSONwriter = new JSONWriter();
            JSONwriter.writeJSONToFile(JSONobject,fileToSave);
        }catch (JSONErrorException ex){
            showAlert("JSON saving Error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
        }catch (IOException ex){
            showAlert("JSON saving Error","File doesn't open.","Please choose correct file.", Alert.AlertType.ERROR);
        }
    }

    public void menuFileSaveAsOnAction(ActionEvent actionEvent) throws IOException, JSONErrorException {
        saveJSONAs();
    }

    public void menuFileSaveOnAction(ActionEvent actionEvent) throws IOException, JSONErrorException {
        saveJSON(openedJSONFile);
    }

    private void saveJSONAs() throws IOException, JSONErrorException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        File file = chooser.showSaveDialog(Main.stage);
        saveJSON(file);
    }

    public void btnRefreshTreeViewOnAction(ActionEvent actionEvent) {
        JSONobject = loadJSONFromTextArea();
        loadJSONToTreeView();
    }
}
