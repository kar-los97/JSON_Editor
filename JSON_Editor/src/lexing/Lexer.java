package lexing;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lexer {
    private Lexer(){
    }

    private static Lexer instance = new Lexer();

    public static Lexer getInstance(){
        return instance;
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
                switch(ch){
                    case '\r':
                    case '\n':
                        break;
                    case ' ':
                    case '\t':
                        addLexem(buffer,row,column,lexems);
                        buffer = "";
                        break;
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case '(':
                    case ')':
                    case ',':
                    case '\'':
                    case ':':
                        addLexem(buffer,row,column,lexems);
                        buffer = "";
                        addLexem(String.valueOf(ch),row,column,lexems);
                        break;
                    case '\"':
                        //je to konec retezce - prida se i retezec
                        addLexem(buffer,row,column,lexems);
                        buffer = "";
                        //pridaji se uvozovky
                        addLexem(String.valueOf(ch),row,column,lexems);
                        isString = !isString;
                        break;
                    default:
                        buffer+=ch;
                        column++;
                        break;
                }
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
