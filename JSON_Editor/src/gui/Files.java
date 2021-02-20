package gui;

import exceptions.JSONErrorException;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import lexing.Lexem;
import lexing.Lexer;
import parsing.IJSONParser;
import parsing.JSONParser;
import reading.IJSONReader;
import reading.JSONReader;
import tokens.Token;
import values.JSONObject;
import writing.IJSONWriter;
import writing.JSONWriter;

import java.io.*;
import java.util.List;
import java.util.Queue;

public class Files {
    private static Files instance = null;

    public static Files getInstance() {
        if(instance == null){
            instance = new Files();
        }
        return instance;
    }

    public void saveJSONAs(String jsonInString){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        File file = chooser.showSaveDialog(Main.stage);
        saveJSON(file,jsonInString);
    }

    public void saveJSON(File fileToSave, String jsonInString){
        if (!isJSONValid(jsonInString)) {
            return;
        }
        IJSONParser jsonParser = new JSONParser();
        Queue<Token> tokens = jsonParser.createTokensFromLexems(Lexer.getInstance().createLexemsFromString(jsonInString));
        try {
            JSONObject JSONobject = jsonParser.parseJSObject(tokens, null);
            IJSONWriter JSONwriter = new JSONWriter();
            JSONwriter.writeJSONToFile(JSONobject,fileToSave);
        }catch (JSONErrorException ex){
            Alerts.getInstance().showAlert("JSON saving Error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
        }catch (IOException ex){
            Alerts.getInstance().showAlert("JSON saving Error","File doesn't open.","Please choose correct file.", Alert.AlertType.ERROR);
        }
    }

    public File openJSON(){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        return chooser.showOpenDialog(Main.stage);
    }
    public JSONObject openJSONFromFile(File openedJSONFile){
        JSONObject jsonObject = null;
        try {
            jsonObject = loadJSONFromFile(openedJSONFile);
        }catch (IOException ex){
            Alerts.getInstance().showAlert("Loading file ERROR","File doesn't open.", "Please choose correct file.", Alert.AlertType.ERROR);
        }catch (JSONErrorException ex){
            Alerts.getInstance().showAlert("JSOUN Loading ERROR","File doesn't load.","Please choose correct file.", Alert.AlertType.ERROR);
        }
        return jsonObject;
    }

    private JSONObject loadJSONFromFile(File openedJSONFile) throws IOException, JSONErrorException {
        IJSONReader jsReader = new JSONReader();
        JSONObject jsonObject = null;
        try{
            String jsonAsString = jsReader.readJSONFromFile(openedJSONFile);
            List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonAsString);
            IJSONParser jsParser = new JSONParser();
            Queue<Token> tokens = jsParser.createTokensFromLexems(lexems);
            jsonObject = jsParser.parseJSObject(tokens,null);
        }catch (NullPointerException ex){
            Alerts.getInstance().showAlert("Opening file ERROR","JSON File is not open","Please open JSON file.", Alert.AlertType.ERROR);
        }
        return jsonObject;
    }

    public boolean isJSONValid(String jsonInString){
        List<Lexem> listOfLexems = Lexer.getInstance().createLexemsFromString(jsonInString);
        IJSONParser parser = new JSONParser();
        Queue<Token> queueOfTokens = parser.createTokensFromLexems(listOfLexems);
        try {
            parser.parseJSObject(queueOfTokens, "");
            return true;
        } catch(JSONErrorException ex){
            Alerts.getInstance().showAlert("JSON validity ERROR","JSON file is not valid",ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }
}
