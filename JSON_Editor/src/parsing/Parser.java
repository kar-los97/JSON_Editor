package parsing;

import enums.TokenType;
import lexing.Lexem;
import netscape.javascript.JSObject;
import tokens.Token;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Parser {
    private Parser(){};
    private static Parser instance = new Parser();
    public static Parser getInstance(){
        return instance;
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

    public JSObject readObject(Queue<Token>){
        return null;
    }

}
