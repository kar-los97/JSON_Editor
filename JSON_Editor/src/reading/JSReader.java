package reading;

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

public class JSReader implements IJSReader {

    public JSReader(){
    }

    public Queue<Token> createTokensFromLexems(List<Lexem> lexems){
        Queue<Token> tokens = new LinkedList<>();
        for(Lexem lexem:lexems){
            tokens.add(createToken(lexem));
        }
        return tokens;
    }

    public Token createToken(Lexem lexem){
        TokenType typeOfNewToken = null;
        for (TokenType tokenType: TokenType.values()) {
            if(lexem.getValue().equals(tokenType.getValue())){
                typeOfNewToken = tokenType;
                break;
            }
        }
        if(typeOfNewToken==null){
            try{
                Double.parseDouble(lexem.getValue());
                typeOfNewToken = TokenType.NUMBER;
            }catch (NumberFormatException ex){
                switch(lexem.getValue()){
                    case "true":
                    case "false":
                        typeOfNewToken = TokenType.BOOLEAN;
                        break;
                    default:
                        typeOfNewToken = TokenType.STRING;
                        break;
                }
            }
        }
        return new Token(typeOfNewToken,lexem.getValue(),lexem.getRow(),lexem.getColumn());
    }

    public JSObject readJSObject(Queue<Token> tokens,String objectName) throws JSONErrorException {
        if(!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException("Curly bracket START expteat ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        JSObject object;
        if(objectName!=null && objectName.length()>0){
            object = new JSObject(objectName);
        }else{
            object = new JSObject();
        }
        readJSObjectValues(tokens,object);
        //ocekava se ukoncovaci zavorka
        if(!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            throw new JSONErrorException("Curly bracket END expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return object;
    }

    private void readJSObjectValues(Queue<Token> tokens, JSObject object) throws JSONErrorException {
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(!tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)&&!tokens.isEmpty()){
            //nacteni nazvu
            String name = readStringValue(tokens);
            //ocekava se :
            if(!tokens.peek().getTypeOfToken().equals(TokenType.COLON)){
                throw new JSONErrorException("Colon expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
            }
            tokens.poll();
            //pokud je slozena zavorka, cti vnoreny objekt
            if (tokens.peek().getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)) {
                object.addValue(readJSObject(tokens,name));
                continue;
            }
            object.addValue(readValue(tokens,name));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
        }
    }

    private String readStringValue(Queue<Token>tokens) throws JSONErrorException {
        if(!tokens.peek().getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        if(!tokens.peek().getTypeOfToken().equals(TokenType.STRING)){
            throw new JSONErrorException("String value expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        String value = tokens.peek().getValue();
        tokens.poll();
        if(!tokens.peek().getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
        tokens.poll();
        return value;
    }

    private boolean readBoolValue(Queue<Token> tokens) throws JSONErrorException {
        switch (tokens.peek().getValue()){
            case "true":
                tokens.poll();
                return true;
            case "false":
                tokens.poll();
                return false;
            default:
                throw new JSONErrorException("Boolean value expected (\"true\" or \"false\" at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
    }

    private double readNumberValue(Queue<Token> tokens) throws JSONErrorException {
        if(tokens.peek().getTypeOfToken().equals(TokenType.NUMBER)){
            return Double.parseDouble(tokens.poll().getValue());
        }
        throw new JSONErrorException("Number value expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private NullValue readNullValue(Queue<Token>tokens, String name) throws JSONErrorException {
        if(tokens.peek().getTypeOfToken().equals(TokenType.NULL)){
            tokens.poll();
            return new NullValue(name);
        }
        throw new JSONErrorException("Null expected at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
    }

    private Value readValue(Queue<Token> tokens, String name) throws JSONErrorException {
        switch (tokens.peek().getTypeOfToken()){
            case SQUARE_BRACKET_START:
                tokens.poll();
                return readJSArray(tokens,name);
            case CURLY_BRACKET_START:
                return readJSObject(tokens,name);
            case NULL:
                return readNullValue(tokens,name);
            case QUONTATION_MARKS:
                return new StringValue(name,readStringValue(tokens));
            case BOOLEAN:
                return new BoolValue(name,readBoolValue(tokens));
            case NUMBER:
                return new NumberValue(name,readNumberValue(tokens));
            default:
                throw new JSONErrorException("Unexpected token at ("+tokens.peek().getRow()+", "+tokens.peek().getColumn()+")");
        }
    }

    private JSArray readJSArray(Queue<Token> tokens, String name) throws JSONErrorException {
        List<Value> values = new ArrayList<>();
        while(!tokens.peek().getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            values.add(readValue(tokens,name));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
        }
        tokens.poll();
        return new JSArray(name,values);
    }

}
