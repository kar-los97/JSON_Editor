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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Files {
    private static Files instance = null;

    public static Files getInstance() {
        if (instance == null) {
            instance = new Files();
        }
        return instance;
    }

    public File saveJSONAs(String jsonInString) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = chooser.showSaveDialog(Main.stage);
        saveJSON(file, jsonInString);
        return file;
    }

    public File saveJSON(File fileToSave, String jsonInString) {
        if (!isJSONValid(jsonInString)) {
            return fileToSave;
        }
        IJSONParser jsonParser = new JSONParser();
        Queue<Token> tokens = jsonParser.createTokensFromLexems(Lexer.getInstance().createLexemsFromString(jsonInString));
        try {
            JSONObject JSONobject = jsonParser.parseJSObject(tokens, null);
            IJSONWriter JSONwriter = new JSONWriter();
            fileToSave = JSONwriter.writeJSONToFile(JSONobject, fileToSave);
        } catch (JSONErrorException ex) {
            Alerts.getInstance().showAlert("JSON saving Error", "JSON File is not valid", ex.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException ex) {
            Alerts.getInstance().showAlert("JSON saving Error", "File doesn't open.", "Please choose correct file.", Alert.AlertType.ERROR);
        }
        return fileToSave;
    }

    public File openJSON() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
        try {
            return chooser.showOpenDialog(Main.stage);
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public JSONObject openJSONFromFile(File openedJSONFile) throws IOException, JSONErrorException {
        JSONObject jsonObject = null;
        jsonObject = loadJSONFromFile(openedJSONFile);
        return jsonObject;
    }

    private JSONObject loadJSONFromFile(File openedJSONFile) throws IOException, JSONErrorException {
        IJSONReader jsReader = new JSONReader();
        JSONObject jsonObject = null;
        try {
            String jsonAsString = jsReader.readJSONFromFile(openedJSONFile);
            List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonAsString);
            IJSONParser jsParser = new JSONParser();
            Queue<Token> tokens = jsParser.createTokensFromLexems(lexems);
            jsonObject = jsParser.parseJSObject(tokens, null);
        } catch (NullPointerException | JSONErrorException ex) {
            throw new JSONErrorException("JSON file is not valid!");
        }
        return jsonObject;
    }

    public boolean isJSONValid(String jsonInString) {
        List<Lexem> listOfLexems = Lexer.getInstance().createLexemsFromString(jsonInString);
        IJSONParser parser = new JSONParser();
        Queue<Token> queueOfTokens = parser.createTokensFromLexems(listOfLexems);
        try {
            parser.parseJSObject(queueOfTokens, "");
            return true;
        } catch (JSONErrorException ex) {
            Alerts.getInstance().showAlert("JSON validity ERROR", "JSON file is not valid", ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }
}
