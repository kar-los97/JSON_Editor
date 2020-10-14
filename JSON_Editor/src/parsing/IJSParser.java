package parsing;

import exceptions.JSONErrorException;
import lexing.Lexem;
import tokens.Token;
import values.JSObject;

import java.util.List;
import java.util.Queue;

public interface IJSParser {
    Queue<Token> createTokensFromLexems(List<Lexem> lexems);
    Token createToken(Lexem l);
    JSObject parseJSObject(Queue<Token> tokens, String objectName) throws JSONErrorException;
}
