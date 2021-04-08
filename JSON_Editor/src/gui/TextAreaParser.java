package gui;

import converting.IJSONConverter;
import converting.JSONConverter;
import exceptions.JSONErrorException;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import lexing.Lexem;
import lexing.Lexer;
import parsing.IJSONParser;
import parsing.JSONParser;
import values.JSONObject;

import java.util.List;

public class TextAreaParser {
    private static TextAreaParser instance = null;

    public static TextAreaParser getInstance() {
        if (instance == null) {
            instance = new TextAreaParser();
        }
        return instance;
    }

    public JSONObject loadJSONFromTextArea(String jsonInString, JSONObject openedObject) {
        try {
            List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonInString);
            IJSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.parseJSObject(jsonParser.createTokensFromLexems(lexems), "");
            return jsonObject;
        } catch (JSONErrorException ex) {
            Alerts.getInstance().showAlert("JSON loading Error", "JSON File is not valid", ex.getMessage(), Alert.AlertType.ERROR);
            return openedObject;
        }
    }

    public String getTextToTextArea(JSONObject JSONobject) {
        IJSONConverter converter = new JSONConverter();
        String jsonInString = "";

        try {
            jsonInString = converter.convertJSON(JSONobject);
        } catch (JSONErrorException ex) {
            Alerts.getInstance().showAlert("JSON Converting ERROR", "Error in converting json to string", ex.getMessage(), Alert.AlertType.ERROR);
        }
        return jsonInString;
    }

    public TextArea createTextAreaFromJsonObject(TextArea textArea, JSONObject JSONobject) {
        textArea.setText(getTextToTextArea(JSONobject));
        return new TextArea(getTextToTextArea(JSONobject));
    }
}
