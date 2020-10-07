package lexing;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private char[] notImportantChars = {'\r', '\n'};
    private char [] whiteSpaceChars = {' ','\t'};
    private char [] separetingChars = {'{', '}','[',']','(', ')', ',', '\'', ':'};

    private Lexer(){
    }

    private static Lexer instance = new Lexer();

    public static Lexer getInstance(){
        return instance;
    }

    private boolean isNotImportantChar(char ch){
        for(int i = 0; i<notImportantChars.length;i++){
            if(notImportantChars[i]==ch){
                return true;
            }
        }
        return false;
    }

    private boolean isWhiteSpaceChar(char ch){
        for(int i = 0; i<whiteSpaceChars.length;i++){
            if(whiteSpaceChars[i]==ch){
                return true;
            }
        }
        return false;
    }

    private boolean isSeparatingChar(char ch){
        for(int i = 0; i<separetingChars.length;i++){
            if(separetingChars[i]==ch){
                return true;
            }
        }
        return false;
    }

    private boolean isQuontatitonMarksChar(char ch){
        return ch=='"';
    }

    public List<Lexem> readLexemsFromFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<Lexem> lexems = new ArrayList<>();
        String buffer = "";
        boolean isString = false;
        int row = 0;
        while(bufferedReader.ready()){
            int column = 0;
            for(char ch : bufferedReader.readLine().toCharArray()){
                if(isNotImportantChar(ch)){
                    continue;
                }
                if(isString && !isQuontatitonMarksChar(ch)){
                    buffer+=ch;
                    column++;
                    continue;
                }
                if(isWhiteSpaceChar(ch)){
                    addLexem(buffer,row,column,lexems);
                    buffer = "";
                    continue;
                }
                if(isSeparatingChar(ch)){
                    addLexem(buffer,row,column,lexems);
                    buffer = "";
                    addLexem(String.valueOf(ch),row,column,lexems);
                    continue;
                }if(isQuontatitonMarksChar(ch)){
                    //je to konec retezce - prida se i retezec
                    addLexem(buffer,row,column,lexems);
                    buffer = "";
                    //pridaji se uvozovky
                    addLexem(String.valueOf(ch),row,column,lexems);
                    isString = !isString;
                    continue;
                }
                buffer+=ch;
                column++;
            }
            addLexem(buffer,row,column,lexems);
            buffer = "";
            row++;
        }
        return lexems;
    }

    private void addLexem(String buffer, int row, int column, List<Lexem> lexems){
        int col = column;
        if(buffer.length()>0){
            if(buffer.length()>1) {
                col = column - buffer.length();
            }
            lexems.add(new Lexem(row,col,buffer));
        }
    }
}
