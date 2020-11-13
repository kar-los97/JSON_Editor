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
import values.*;
import converting.*;
import writing.*;
import java.io.*;
import java.util.*;

public class MainWindowController {
    private JSONObject JSONobject;
    private File openedJSONFile;
    @FXML
    private TreeView<String> treeJS;
    @FXML
    private TextArea textAreaJSON;

    @FXML
    private void initialize() {
        /*Main.stage.setOnCloseRequest(event -> {
                    try {
                        closeApp();
                    } catch (JSONErrorException e) {
                       showAlert("JSON Error","JSON Error exception",e.getMessage(), Alert.AlertType.WARNING);
                    } catch (IOException e) {
                        showAlert("IOException","File error",e.getMessage(), Alert.AlertType.WARNING);
                    }
                }
        );*/
    }

    private Optional<ButtonType> showAlert(String title, String headerText, String contentText, Alert.AlertType alertType){
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
        IJSONReader jsReader = new JSONReader();
        String jsonAsString = jsReader.readJSONFromFile(openedJSONFile);
        List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonAsString);
        IJSONParser jsParser = new JSONParser();
        Queue<Token> tokens = jsParser.createTokensFromLexems(lexems);
        JSONobject = jsParser.parseJSObject(tokens,null);

        TreeItem<String> rootItem = new TreeItem<> ("{");
        rootItem.setExpanded(true);
        for (JSONValue v: JSONobject.getValue()) {
            if(v instanceof JSONArray){
                TreeItem<String> trItem = new TreeItem<>("\""+v.getName()+"\""+": [");
                for (JSONValue va:(List<JSONValue>)v.getValue()) {
                    trItem.getChildren().add(new TreeItem<>(""+va.getValue()));
                }
                rootItem.getChildren().add(trItem);
            }else if (v instanceof JSONObject){
                TreeItem<String> trItem = new TreeItem<>("\""+v.getName()+"\""+": {");
                for(JSONValue va:(List<JSONValue>)v.getValue()){
                    trItem.getChildren().add(new TreeItem<>("\""+va.getName()+"\""+": "+va.getValue()));
                }
                rootItem.getChildren().add(trItem);
            }else{
                rootItem.getChildren().add(new TreeItem<>("\""+v.getName()+"\""+": "+v.getValue()));
            }
        }
        treeJS.setRoot(rootItem);
        IJSONConverter JSONConverter = new JSONConverter();
        textAreaJSON.setText(JSONConverter.convertJSON(JSONobject));
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

    private void saveJSON(File fileToSave) throws IOException, JSONErrorException {
        IJSONParser jsonParser = new JSONParser();
        Queue<Token> tokens = jsonParser.createTokensFromLexems(Lexer.getInstance().createLexemsFromString(textAreaJSON.getText()));
        try {
            JSONobject = jsonParser.parseJSObject(tokens, null);
        }catch (JSONErrorException ex){
            showAlert("JSON saving Error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
            JSONobject = null;
        }
        if(JSONobject!=null){
            IJSONWriter JSONWriter = new JSONWriter();
            JSONWriter.writeJSONToFile(JSONobject,fileToSave);
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
}
