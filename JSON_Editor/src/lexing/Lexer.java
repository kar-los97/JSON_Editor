package lexing;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

    public List<Lexem> readLexemsFromFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        List<Lexem> lexems = new LinkedList<>();
        String buffer = "";
        boolean isString = false;
        int row = 0;
        while(bufferedReader.ready()){
            int column = 0;
            for(char ch : bufferedReader.readLine().toCharArray()){
                if(isString && ch!='\"'){
                    buffer+=ch;
                    column++;
                    continue;
                }
                if(isNotImportantChar(ch)){
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
                }if(ch=='\"'){
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
