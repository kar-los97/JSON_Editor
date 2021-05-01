package gui;

import converting.*;
import exceptions.JSONErrorException;
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

    public JSONObject loadJSONFromTextArea(String jsonInString, JSONObject openedObject) throws JSONErrorException {
        try {
            List<Lexem> lexems = Lexer.getInstance().createLexemsFromString(jsonInString);
            IJSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.parseJSObject(jsonParser.createTokensFromLexems(lexems), "");
            return jsonObject;
        } catch (JSONErrorException ex) {
            throw new JSONErrorException("Error at loading json from text area.");
        }
    }

    public String getTextToTextArea(JSONObject JSONobject) throws JSONErrorException {
        IJSONConverter converter = new JSONConverter();
        String jsonInString = "";

        jsonInString = converter.convertJSON(JSONobject);
        return jsonInString;
    }
}
