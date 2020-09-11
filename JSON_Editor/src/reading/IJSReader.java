package reading;

import exceptions.JSONErrorException;
import lexing.Lexem;
import tokens.Token;
import values.jsarray.JSArray;
import values.JSObject;

import java.util.List;
import java.util.Queue;

public interface IJSReader {
    Queue<Token> createTokensFromLexems(List<Lexem> lexems);
    Token createToken(Lexem l);
    JSObject readJSObject(Queue<Token> tokens) throws JSONErrorException;
}
