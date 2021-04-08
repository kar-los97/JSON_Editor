package lexing;


import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private char[] notImportantChars = {'\r', '\n'};
    private char[] whiteSpaceChars = {' ', '\t'};
    private char[] separetingChars = {'{', '}', '[', ']', '(', ')', ',', '\'', ':'};

    private Lexer() {
    }

    private static Lexer instance = new Lexer();

    public static Lexer getInstance() {
        return instance;
    }

    private boolean foundCharacter(char[] characters, char character) {
        for (int i = 0; i < characters.length; i++) {
            if (characters[i] == character) {
                return true;
            }
        }
        return false;
    }

    private boolean isNotImportantChar(char ch) {
        return foundCharacter(notImportantChars, ch);
    }

    private boolean isWhiteSpaceChar(char ch) {
        return foundCharacter(whiteSpaceChars, ch);
    }

    private boolean isSeparatingChar(char ch) {
        return foundCharacter(separetingChars, ch);
    }

    private boolean isQuontatitonMarksChar(char ch) {
        return ch == '"';
    }

    public List<Lexem> createLexemsFromString(String jsonAsString) {
        List<Lexem> lexems = new ArrayList<>();
        String buffer = "";
        boolean isString = false;
        int row = 0;
        int column = 0;
        for (char ch : jsonAsString.toCharArray()) {
            if (isNotImportantChar(ch)) {
                row++;
                column = 0;
                continue;
            }
            if (isString && !isQuontatitonMarksChar(ch)) {
                buffer += ch;
                column++;
                continue;
            }
            if (isWhiteSpaceChar(ch)) {
                addLexem(buffer, row, column, lexems);
                buffer = "";
                continue;
            }
            if (isSeparatingChar(ch)) {
                addLexem(buffer, row, column, lexems);
                buffer = "";
                addLexem(String.valueOf(ch), row, column, lexems);
                continue;
            }
            if (isQuontatitonMarksChar(ch)) {
                //je to konec retezce - prida se i retezec
                addLexem(buffer, row, column, lexems);
                buffer = "";
                //pridaji se uvozovky
                addLexem(String.valueOf(ch), row, column, lexems);
                isString = !isString;
                continue;
            }
            buffer += ch;
            column++;
        }
        addLexem(buffer, row, column, lexems);
        return lexems;
    }

    private void addLexem(String buffer, int row, int column, List<Lexem> lexems) {
        int col = column;
        if (buffer.length() > 0) {
            if (buffer.length() > 1) {
                col = column - buffer.length();
            }
            lexems.add(new Lexem(row, col, buffer));
        }
    }
}
