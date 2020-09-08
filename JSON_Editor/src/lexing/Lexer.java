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
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<Lexem> lexems = new LinkedList<>();
        String buffer = "";
        boolean isString = false;
        int row = 0;
        while(br.ready()){
            int column = 0;
            for(char ch : br.readLine().toCharArray()){
                if(isString && ch!='\"'){
                    buffer+=ch;
                    column++;
                    continue;
                }
                switch(ch){
                    case '\t':
                        break;
                    case ' ':
                    case '{':
                    case '}':
                    case '[':
                    case ']':
                    case '(':
                    case ')':
                    case ',':
                    case '\'':
                    case ':':
                        int col = column;
                        if(buffer.length()>0){
                            if(buffer.length()>1) {
                                col = column - buffer.length();
                            }
                            lexems.add(new Lexem(row,col,buffer));
                            buffer = "";
                        }
                        lexems.add(new Lexem(row,column,String.valueOf(ch)));
                        break;
                    case '\"':
                        if(buffer.length()>0){
                            lexems.add(new Lexem(row,column-buffer.length(),buffer));
                            buffer = "";
                        }
                        lexems.add(new Lexem(row,column,String.valueOf(ch)));
                        isString = !isString;
                        break;
                    default:
                        buffer+=ch;
                        column++;
                        break;
                }

            }
            if(buffer.length()>0){
                int col = column;
                if(buffer.length()>1)
                    col = column-buffer.length();
                lexems.add(new Lexem(row,col,buffer));
                buffer = "";
            }
            row++;
        }
        return lexems;
    }
}
