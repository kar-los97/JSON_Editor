package parsing;

import exceptions.JSONErrorException;
import lexing.Lexem;
import tokens.Token;
import values.JSONObject;

import java.util.List;
import java.util.Queue;

public interface IJSONParser {
    Queue<Token> createTokensFromLexems(List<Lexem> lexems);

    Token createToken(Lexem l);

    JSONObject parseJSObject(Queue<Token> tokens, String objectName) throws JSONErrorException;
}
