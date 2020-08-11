package reading;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import values.JSObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tokens.Token;
import values.JSArray;
import values.NumberValue;
import values.StringValue;
import values.Value;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JSReader implements IJSReader {

    public JSReader(){

    }

    public Queue<Token> createTokensFromLexems(List<Lexem> lexems){
        Queue<Token> tokens = new LinkedList<>();
        for(Lexem l:lexems){
            tokens.add(createToken(l));
        }
        return tokens;
    }

    public Token createToken(Lexem l){
        TokenType type=null;
        String value;
        for (TokenType tt: TokenType.values()) {
            if(l.getValue().equals(tt.getValue())){
                type = tt;
                break;
            }
        }
        if(type==null){
            try{
                Double.parseDouble(l.getValue());
                type = TokenType.NUMBER;
            }catch (NumberFormatException ex){
                type = TokenType.STRING;
            }
        }

        Token t = new Token(type,l.getValue(),l.getRow(),l.getColumn());
        return t;
    }

    public JSObject readObject(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException();
        }
        tokens.poll();
        t = tokens.poll();
        JSObject object = new JSObject();
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)&&!tokens.isEmpty()){
            object.addValue(readValue(tokens));
        }
        throw new NotImplementedException();

    }

    public Value readValue(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if (t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)) {
            return readObject(tokens);
        }
        //nazev
        String name;
        //hodnota objektu - začíná vždy názevem, který je mezi uvozovkami
        if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
        }

        tokens.poll();
        t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.STRING)){
            throw new JSONErrorException("String value expected at ("+t.getRow()+", "+t.getColumn()+")");
        }
        name = t.getValue();

        tokens.poll();
        t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
        }

        tokens.poll();
        t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.COLON)){
            throw new JSONErrorException("Colon expected at ("+t.getRow()+", "+t.getColumn()+")");
        }
        tokens.poll();
        t = tokens.peek();
        switch (t.getTypeOfToken()){
            case SQUARE_BRACKET_START:
                return readJSArray(tokens,name);
        }
        throw new NotImplementedException();
    }

    public JSArray readJSArray(Queue<Token> tokens,String name){
        throw new NotImplementedException();
    }

    public NumberValue readNumberValue(Queue<Token> tokens, String name){
        throw new NotImplementedException();
    }

    public StringValue readStringValue(Queue<Token>tokens, String name){
        throw new NotImplementedException();
    }

}
