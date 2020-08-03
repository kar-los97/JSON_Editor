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
        while(br.ready()){
            for(char ch : br.readLine().toCharArray()){
                if(isString && ch!='\"'){
                    buffer+=ch;
                    continue;
                }
                switch(ch){
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
                        if(buffer.length()>0){
                            lexems.add(new Lexem(buffer));
                            buffer = "";
                        }
                        lexems.add(new Lexem(String.valueOf(ch)));
                        break;
                    case '\"':
                        if(buffer.length()>0){
                            lexems.add(new Lexem(buffer));
                            buffer = "";
                        }
                        lexems.add(new Lexem(String.valueOf(ch)));
                        isString = !isString;
                        break;
                    default:
                        buffer+=ch;
                        break;
                }
            }
        }
        return lexems;
    }
}
