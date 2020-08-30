package reading;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import values.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tokens.Token;

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

    public JSObject readObject(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException();
        }
        tokens.poll();
        t = tokens.peek();
        JSObject object = new JSObject();
        //dokud neni ukoncovaci zavorka - cti hodnoty
        while(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)&&!tokens.isEmpty()){
            object.addValue(readValue(tokens));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
            t = tokens.peek();
        }
        return object;

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
        if(t.getTypeOfToken().equals(TokenType.SQUARE_BRACKET_START)){
            return readJSArray(tokens,name);
        }else if (t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
            tokens.poll();
            t = tokens.peek();
        }
        switch (t.getTypeOfToken()){
            case SQUARE_BRACKET_START:
                return readJSArray(tokens,name);
            case CURLY_BRACKET_START:
                return readObject(tokens);
            case NULL:
                return new NullValue(name);
            case STRING:
                StringValue strVal = new StringValue(name,t.getValue());
                tokens.poll();
                t = tokens.peek();
                if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
                    throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
                }
                tokens.poll();
                return strVal;
            case BOOLEAN:
                BoolValue boolValue = new BoolValue(name,Boolean.parseBoolean(t.getValue()));
                tokens.poll();
                t = tokens.peek();
                if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){
                    throw new JSONErrorException("Quontation mark expected at ("+t.getRow()+", "+t.getColumn()+")");
                }
                tokens.poll();
                return boolValue;
            case NUMBER:
                NumberValue nValue = new NumberValue(name,Double.parseDouble(t.getValue()));
                tokens.poll();
                return nValue;
            default:
                throw new JSONErrorException("Unexpected token at ("+t.getRow()+", "+t.getColumn()+")");
        }
    }

    public JSArray readJSArray(Queue<Token> tokens,String name) throws JSONErrorException {
        List<Value> values = new ArrayList<>();
        Token t = tokens.peek();
        while(t.getTypeOfToken().equals(TokenType.SQUARE_BRACKET_END)){
            values.add(readValue(tokens));
            if(tokens.peek().getTypeOfToken().equals(TokenType.COMMA)){
                tokens.poll();
            }
            t = tokens.peek();
        }
        return new JSArray(name,values);
    }

}
