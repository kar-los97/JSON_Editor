package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("JSON editor");
        primaryStage.setScene(new Scene(root, 300, 275));
        stage = primaryStage;
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
