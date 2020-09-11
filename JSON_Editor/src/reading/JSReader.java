package reading;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import values.*;
import tokens.Token;
import values.jsarray.JSArray;
import values.jsarray.JSArrayValue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class JSReader implements IJSReader {

    public JSReader(){

    }

    public Queue<Token> createTokensFromLexems(List<Lexem> lexems){
        Queue<Token> tokens = new LinkedList<>();
        for(Lexem l:lexems){
            Token t = createToken(l);
            if(t.getTypeOfToken().equals(TokenType.WHITE_SPACE)){
                continue;
            }
            tokens.add(createToken(l));
        }
        return tokens;
    }

    public Token createToken(Lexem l){
        TokenType type=null;
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
                switch(l.getValue()){
                    case "\t":
                    case " ":
                    case "\n":
                        type = TokenType.WHITE_SPACE;
                        break;
                    case "true":
                    case "false":
                        type = TokenType.BOOLEAN;
                        break;
                    default:
                        type = TokenType.STRING;
                        break;
                }

            }
        }

        Token t = new Token(type,l.getValue(),l.getRow(),l.getColumn());
        return t;
    }

    public JSObject readJSObject(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException();
        }
        tokens.poll();
        t = tokens.peek();
        JSObject object = new JSObject();
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)&&!tokens.isEmpty()){
            t = tokens.peek();
            //pokud je slozena zavorka, cti vnoreny objekt
            if (t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)) {
                object.addValue(readJSObject(tokens));
                t = tokens.peek();
                continue;
            }
            //nacteni nazvu
            String name = readStringValue(tokens);
            //ocekava se :
            t = tokens.peek();
            if(!t.getTypeOfToken().equals(TokenType.COLON)){
                throw new JSONErrorException("Colon expected at ("+t.getRow()+", "+t.getColumn()+")");
            }
            tokens.poll();
            object.addValue(readValue(tokens,name));
            t = tokens.peek();

            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
            t = tokens.peek();
        }
        return object;

    }

    private String readStringValue(Queue<Token>tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
        }
        tokens.poll();
        t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.STRING)){
            throw new JSONErrorException("String value expected at ("+t.getRow()+", "+t.getColumn()+")");
        }
        String value = t.getValue();
        tokens.poll();
        t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
        }
        tokens.poll();
        return value;
    }

    private boolean readBoolValue(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        switch (t.getValue()){
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new JSONErrorException("Boolean value expected (\"true\" or \"false\" at ("+t.getRow()+", "+t.getColumn()+")");
        }
    }

    private double readNumberValue(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(t.getTypeOfToken().equals(TokenType.NUMBER)){
            tokens.poll();
            return Double.parseDouble(t.getValue());
        }
        throw new JSONErrorException("Number value expected at ("+t.getRow()+", "+t.getColumn()+")");
    }

    private Value readValue(Queue<Token> tokens, String name) throws JSONErrorException {
        Token t = tokens.peek();
        switch (t.getTypeOfToken()){
            case SQUARE_BRACKET_START:
                tokens.poll();
                return readJSArray(tokens,name);
            case CURLY_BRACKET_START:
                return readJSObject(tokens);
            case NULL:
                return new NullValue(name);
            case QUONTATION_MARKS:
                return new StringValue(name,readStringValue(tokens));
            case BOOLEAN:
                return new BoolValue(name,readBoolValue(tokens));
            case NUMBER:
                return new NumberValue(name,readNumberValue(tokens));
            default:
                throw new JSONErrorException("Unexpected token at ("+t.getRow()+", "+t.getColumn()+")");
        }
    }

    private JSArray readJSArray(Queue<Token> tokens, String name) throws JSONErrorException {
        List<JSArrayValue> values = new ArrayList<>();
        Token t = tokens.peek();
        while(!t.getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            values.add(readJSArrayValue(tokens));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
            t = tokens.peek();
        }
        tokens.poll();
        return new JSArray(name,values);
    }

    private JSArrayValue readJSArrayValue(Queue<Token> tokens)throws JSONErrorException{
        Token t = tokens.peek();
        if(t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            tokens.poll();
            t = tokens.peek();
        }
        throw new NotImplementedException();
    }

}
