package parsing;

import enums.TokenType;
import exceptions.JSONErrorException;
import lexing.Lexem;
import netscape.javascript.JSObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tokens.Token;
import values.JSArray;
import values.NumberValue;
import values.StringValue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Parser implements IParser{

    public Parser(){

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
                type = TokenType.OTHER;
            }
        }

        Token t = new Token(type,l.getValue());
        return t;
    }

    public JSObject readObject(Queue<Token> tokens) throws JSONErrorException {
        Token t = tokens.peek();
        if(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_START)){
            throw new JSONErrorException();
        }
        tokens.poll();
        t = tokens.poll();
        //dokud neni ukoncovaci zavorka - cti objekt
        while(!t.getTypeOfToken().equals(TokenType.CURLY_BRACKET_END)){
            //hodnota objektu - začíná vždy názevem, který je mezi uvozovkami
            if(!t.getTypeOfToken().equals(TokenType.QUONTATION_MARKS)){

            }
        }
        throw new NotImplementedException();

    }

    public JSArray createJSArray(Queue<Token> tokens){
        throw new NotImplementedException();
    }

    public NumberValue createNumberValue(Queue<Token> tokens){
        throw new NotImplementedException();
    }

    public StringValue createStringValue(Queue<Token>tokens){
        throw new NotImplementedException();
    }

}
