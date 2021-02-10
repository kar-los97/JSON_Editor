package parsing;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import values.*;
import tokens.Token;
import values.JSONArray;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JSONParser implements IJSONParser {

    public JSONParser(){
    }

    private boolean verifyTokensQueue(Queue<Token>tokens){
        return !tokens.isEmpty()||tokens!=null;
    }

    public Queue<Token> createTokensFromLexems(List<Lexem> lexems){
        Queue<Token> tokens = new LinkedList<>();
        for(Lexem lexem:lexems){
            tokens.add(createToken(lexem));
        }
        return tokens;
    }

    private boolean isLexemNumber(Lexem lexem){
        try{
            Double.parseDouble(lexem.getValue());
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }

    private boolean isLexemBoolean(Lexem lexem){
        switch(lexem.getValue()){
            case "true":
            case "false":
                return true;
            default:
                return false;
        }
    }

    private TokenType getTypicalTypeOfToken(Lexem lexem){
        for (TokenType tokenType: TokenType.values()) {
            if(lexem.getValue().equals(tokenType.getValue())){
                return tokenType;
            }
        }
        return TokenType.STRING;
    }

    private TokenType getTokenTypeFromLexem(Lexem lexem){
        if(isLexemNumber(lexem)){
            return TokenType.NUMBER;
        }if(isLexemBoolean(lexem)){
            return TokenType.BOOLEAN;
        }
        return getTypicalTypeOfToken(lexem);
    }

    public Token createToken(Lexem lexem){
        TokenType typeOfNewToken = getTokenTypeFromLexem(lexem);
        return new Token(typeOfNewToken,lexem.getValue(),lexem.getRow(),lexem.getColumn());
    }

    public JSONObject parseJSObject(Queue<Token> tokens, String objectName) throws JSONErrorException {
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException("Curly bracket START expteat ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        JSONObject object;
        if(objectName!=null && objectName.length()>0){
            object = new JSONObject(objectName);
        }else{
            object = new JSONObject();
        }
        parseJSObjectValues(tokens,object);
        //ocekava se ukoncovaci zavorka
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            throw new JSONErrorException("Curly bracket END expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return object;
    }

    private void parseJSObjectValues(Queue<Token> tokens, JSONObject object) throws JSONErrorException {
        //resi, zda objekt neni prazdny
        if(tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            return;
        }
        String name = parseStringValue(tokens);
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.COLON)){
            throw new JSONErrorException("Colon expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        object.addValue(parseValue(tokens,name));
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(verifyTokensQueue(tokens)&&tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
            tokens.poll();
            //nacteni nazvu
             name = parseStringValue(tokens);
            //ocekava se :
            if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.COLON)){
                throw new JSONErrorException("Colon expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
            }
            tokens.poll();
            //pokud je slozena zavorka, cti vnoreny objekt
            if (verifyTokensQueue(tokens)&&tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)) {
                object.addValue(parseJSObject(tokens,name));
                continue;
            }
            object.addValue(parseValue(tokens,name));
        }
        if(verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            throw new JSONErrorException("Curly bracket END expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
    }

    private String parseStringValue(Queue<Token>tokens) throws JSONErrorException {
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.STRING)){
            throw new JSONErrorException("String value expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        String value = tokens.peek().getValue();
        tokens.poll();
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return value;
    }

    private boolean parseBoolValue(Queue<Token> tokens) throws JSONErrorException {
        if(verifyTokensQueue(tokens)&&tokens.peek().getTypeOfToken().equals(TokenType.BOOLEAN)){
            boolean boolValue = Boolean.parseBoolean(tokens.peek().getValue());
            tokens.poll();
            return boolValue;
        }
        throw new JSONErrorException("Boolean value expected (\"true\" or \"false\" at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private double parseNumberValue(Queue<Token> tokens) throws JSONErrorException {
        if(tokens.peek().getTypeOfToken().equals(TokenType.NUMBER)){
            return Double.parseDouble(tokens.poll().getValue());
        }
        throw new JSONErrorException("Number value expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private JSONNullJSONValue parseNullValue(Queue<Token>tokens, String name) throws JSONErrorException {
        if(tokens.peek().getTypeOfToken().equals(TokenType.NULL)){
            tokens.poll();
            return new JSONNullJSONValue(name);
        }
        throw new JSONErrorException("Null expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private JSONValue parseValue(Queue<Token> tokens, String name) throws JSONErrorException {
        switch (tokens.peek().getTypeOfToken()){
            case SQUARE_BRACKET_START:
                return parseJSArray(tokens,name);
            case CURLY_BRACKET_START:
                return parseJSObject(tokens,name);
            case NULL:
                return parseNullValue(tokens,name);
            case QUONTATION_MARKS:
                return new JSONStringJSONValue(name, parseStringValue(tokens));
            case BOOLEAN:
                return new JSONBoolJSONValue(name, parseBoolValue(tokens));
            case NUMBER:
                return new JSONNumberJSONValue(name, parseNumberValue(tokens));
            default:
                throw new JSONErrorException("Unexpected token at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
    }

    private JSONArray parseJSArray(Queue<Token> tokens, String name) throws JSONErrorException {
        if(!tokens.peek().getTypeOfToken().equals(TokenType.SQUARE_BRACKET_START)){
            throw new JSONErrorException("SQUARE BRACKET START expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        List<JSONValue> JSONValues = new ArrayList<>();
        while(!tokens.peek().getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            JSONValues.add(parseValue(tokens,name));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }else{
                break;
            }
        }
        if(!tokens.peek().getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            throw new JSONErrorException("SQUARE BRACKET END expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return new JSONArray(name, JSONValues);
    }

}
