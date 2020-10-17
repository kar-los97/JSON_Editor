package parsing;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import values.*;
import tokens.Token;
import values.JSArray;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JSParser implements IJSParser {

    public JSParser(){
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

    public JSObject parseJSObject(Queue<Token> tokens, String objectName) throws JSONErrorException {
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException("Curly bracket START expteat ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        JSObject object;
        if(objectName!=null && objectName.length()>0){
            object = new JSObject(objectName);
        }else{
            object = new JSObject();
        }
        parseJSObjectValues(tokens,object);
        //ocekava se ukoncovaci zavorka
        if(!verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            throw new JSONErrorException("Curly bracket END expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return object;
    }

    private void parseJSObjectValues(Queue<Token> tokens, JSObject object) throws JSONErrorException {
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(verifyTokensQueue(tokens)&&!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)&&!tokens.isEmpty()){
            //nacteni nazvu
            String name = parseStringValue(tokens);
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
            if(verifyTokensQueue(tokens)&&tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
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

    private NullValue parseNullValue(Queue<Token>tokens, String name) throws JSONErrorException {
        if(tokens.peek().getTypeOfToken().equals(TokenType.NULL)){
            tokens.poll();
            return new NullValue(name);
        }
        throw new JSONErrorException("Null expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private Value parseValue(Queue<Token> tokens, String name) throws JSONErrorException {
        switch (tokens.peek().getTypeOfToken()){
            case SQUARE_BRACKET_START:
                tokens.poll();
                return parseJSArray(tokens,name);
            case CURLY_BRACKET_START:
                return parseJSObject(tokens,name);
            case NULL:
                return parseNullValue(tokens,name);
            case QUONTATION_MARKS:
                return new StringValue(name, parseStringValue(tokens));
            case BOOLEAN:
                return new BoolValue(name, parseBoolValue(tokens));
            case NUMBER:
                return new NumberValue(name, parseNumberValue(tokens));
            default:
                throw new JSONErrorException("Unexpected token at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
    }

    private JSArray parseJSArray(Queue<Token> tokens, String name) throws JSONErrorException {
        List<Value> values = new ArrayList<>();
        while(!tokens.peek().getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            values.add(parseValue(tokens,name));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
        }
        tokens.poll();
        return new JSArray(name,values);
    }

}
