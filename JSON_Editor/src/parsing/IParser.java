package parsing;

import exceptions.JSONErrorException;
import lexing.Lexem;
import netscape.javascript.JSObject;
import tokens.Token;
import values.JSArray;
import values.NumberValue;
import values.StringValue;

import java.util.List;
import java.util.Queue;

public interface IParser {
    Queue<Token> createTokensFromLexems(List<Lexem> lexems);
    Token createToken(Lexem l);
    JSObject readObject(Queue<Token> tokens) throws JSONErrorException;
    JSArray createJSArray(Queue<Token> tokens);
    NumberValue createNumberValue(Queue<Token> tokens);
    StringValue createStringValue(Queue<Token>tokens);
}
