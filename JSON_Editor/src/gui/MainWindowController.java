package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import validating.TextChangeChecking;
import values.JSONObject;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindowController {
    public HBox hboxTreeText;
    public AnchorPane paneCodeArea;
    public ScrollPane scrollPane;
    public MenuItem menuFileSave;
    public MenuItem menuFileOpen;
    public MenuItem menuFileSaveAs;
    public MenuItem menuEditFind;
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
        menuFileSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN));
        menuFileOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN));
        menuFileSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN));
        menuEditFind.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCodeCombination.CONTROL_DOWN));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        /*paneCodeArea.getChildren().add(virtualizedScrollPane);
        textAreaJSON.prefHeightProperty().bind(virtualizedScrollPane.heightProperty());
        textAreaJSON.prefWidthProperty().bind(virtualizedScrollPane.widthProperty());*/
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
        String findingItem = "";
        try{
            findingItem = textInputDialog.showAndWait().get();
            if (!findingItem.isEmpty()) {
                int index = textAreaJSON.getText().indexOf(findingItem);
                textAreaJSON.selectRange(index, index + findingItem.length());
            }
        }catch (NoSuchElementException ex){

        }
    }
}
