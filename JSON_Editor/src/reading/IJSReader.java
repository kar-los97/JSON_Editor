package reading;

import exceptions.JSONErrorException;
import lexing.Lexem;
import tokens.Token;
import values.JSArray;
import values.JSObject;
import values.NumberValue;
import values.StringValue;

import java.util.List;
import java.util.Queue;

public interface IJSReader {
    Queue<Token> createTokensFromLexems(List<Lexem> lexems);
    Token createToken(Lexem l);
    JSObject readObject(Queue<Token> tokens) throws JSONErrorException;
    JSArray readJSArray(Queue<Token> tokens,String name);
}
