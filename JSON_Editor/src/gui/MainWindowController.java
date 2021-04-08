package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import validating.TextChangeChecking;
import values.JSONObject;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class MainWindowController {
    public HBox hboxTreeText;
    public AnchorPane paneCodeArea;
    private Thread threadToTextCheck;
    private TextChangeChecking textChangeChecking;
    private JSONObject JSONobject;
    private File openedJSONFile;
    @FXML
    private TreeView<String> treeJS;
    @FXML
    private CodeArea textAreaJSON;

    @FXML
    private void initialize() {
        paneCodeArea.getChildren().add(new VirtualizedScrollPane<>(textAreaJSON));
        textAreaJSON.prefHeightProperty().bind(paneCodeArea.heightProperty());
        textAreaJSON.prefWidthProperty().bind(paneCodeArea.widthProperty());
        textAreaJSON.setParagraphGraphicFactory(LineNumberFactory.get(textAreaJSON));
        textChangeChecking = new TextChangeChecking(LocalDateTime.now(), "");
        threadToTextCheck = new Thread(textChangeChecking);
        threadToTextCheck.start();
        textAreaJSON.textProperty().addListener((observable, oldValue, newValue) -> {
            textChangeChecking.setLastTimeTextChanged(LocalDateTime.now());
            textChangeChecking.setStringOfJSONFile(textAreaJSON.getText());
            if (!threadToTextCheck.isAlive()) {
                threadToTextCheck = new Thread(textChangeChecking);
                threadToTextCheck.start();
            }
        });
    }

    public void menuFileOpenOnAction(ActionEvent actionEvent) throws IOException {
        File newFile = Files.getInstance().openJSON();
        if (newFile != null) {
            openedJSONFile = newFile;
            JSONobject = Files.getInstance().openJSONFromFile(openedJSONFile);
            treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS, JSONobject);
            textAreaJSON.replaceText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
        }
    }

    public void menuFileCloseOnAction(ActionEvent actionEvent) {
        closeApp();
    }

    public void menuFileSaveAsOnAction(ActionEvent actionEvent) {
        Files.getInstance().saveJSONAs(textAreaJSON.getText());
    }

    public void menuFileSaveOnAction(ActionEvent actionEvent) {
        Files.getInstance().saveJSON(openedJSONFile, textAreaJSON.getText());
    }

    private void closeApp() {
        if (JSONobject != null) {
            Optional<ButtonType> optionalBtn = Alerts.getInstance().showAlert("Closing", "Are you sure that you want close the file?", "Do you want save the file before close it?", Alert.AlertType.CONFIRMATION);
            if (optionalBtn.get().equals(ButtonType.OK)) {
                Files.getInstance().saveJSONAs(textAreaJSON.getText());
            }
        }
        Main.stage.close();
    }

    public void btnRefreshTreeViewOnAction(ActionEvent actionEvent) {
        JSONobject = TextAreaParser.getInstance().loadJSONFromTextArea(textAreaJSON.getText(), JSONobject);
        treeJS = TreeViewParser.getInstance().loadJSONToTreeView(treeJS, JSONobject);
        textAreaJSON.replaceText(TextAreaParser.getInstance().getTextToTextArea(JSONobject));
    }

    public void menuItemEditFind(ActionEvent actionEvent) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Find");
        textInputDialog.setHeaderText("Find in JSON");
        textInputDialog.setContentText("What are you finding?");
        String findingItem = textInputDialog.showAndWait().get();
        if (findingItem != null) {
            int index = textAreaJSON.getText().indexOf(findingItem);
            textAreaJSON.selectRange(index, index + findingItem.length());
        }
    }
}
