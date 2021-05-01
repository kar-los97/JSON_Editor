package validating;

import exceptions.JSONErrorException;
import gui.Alerts;
import gui.Main;
import gui.MainWindowController;
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
    public void run() {
        while (lastTimeTextChanged.plusSeconds(5).compareTo(LocalDateTime.now()) > 0) {
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
            Platform.runLater(() -> {
                MainWindowController controller = Main.getLoader().getController();
                controller.loadJsonToTree();
            });
        } catch (JSONErrorException ex) {
            Platform.runLater(() -> {
                Alerts.getInstance().showAlert("JSON parsing error", "JSON File is not valid", ex.getMessage(), Alert.AlertType.ERROR);
            });
        }

    }
}
