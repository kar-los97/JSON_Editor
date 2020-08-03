package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lexing.Lexem;
import lexing.Lexer;
import parsing.Parser;
import tokens.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        stage = primaryStage;
        primaryStage.show();
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        File f = chooser.showOpenDialog(stage);
        List<Lexem> lexems = Lexer.getInstance().readLexemsFromFile(f);
        Queue<Token> tokens = Parser.getInstance().createTokensFromLexems(lexems);
        for(Token t:tokens){
            System.out.println(t);
        }
        //System.out.println(lexems);
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
