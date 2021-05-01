package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage stage;
    public static Scene scene;
    private static FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception {
        loader = new FXMLLoader(Main.class.getResource("main_window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("JSON editor");
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(910);
        stage = primaryStage;
        MainWindowController controller = loader.getController();
        primaryStage.setOnCloseRequest(event -> {
            controller.closeApp(event);
        });
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public static FXMLLoader getLoader() {
        return loader;
    }
}
