package validating;

import exceptions.JSONErrorException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lexing.Lexem;
import lexing.Lexer;
import parsing.IJSONParser;
import parsing.JSONParser;
import tokens.Token;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Queue;

public class TextChangeChecking implements Runnable {
    private LocalDateTime lastTimeTextChanged;
    private String stringOfJSONFile;

    public TextChangeChecking(LocalDateTime lastTimeTextChanged, String stringOfJSONFile) {
        this.lastTimeTextChanged = lastTimeTextChanged;
        this.stringOfJSONFile = stringOfJSONFile;
    }

    public void setLastTimeTextChanged(LocalDateTime lastTimeTextChanged) {
        this.lastTimeTextChanged = lastTimeTextChanged;
    }

    public void setStringOfJSONFile(String stringOfJSONFile) {
        this.stringOfJSONFile = stringOfJSONFile;
    }

    @Override
    public void run(){
        while(lastTimeTextChanged.plusSeconds(5).compareTo(LocalDateTime.now())>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        List<Lexem> listOfLexems = Lexer.getInstance().createLexemsFromString(stringOfJSONFile);
        IJSONParser parser = new JSONParser();
        Queue<Token> listOfTokens = parser.createTokensFromLexems(listOfLexems);
        try {
            parser.parseJSObject(listOfTokens, "");
        }catch (JSONErrorException ex){
            Platform.runLater(() -> {
                //System.out.println(ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(ex.getMessage());
                alert.setTitle("JSON parsing error");
                alert.showAndWait();
            });
        }

    }
}