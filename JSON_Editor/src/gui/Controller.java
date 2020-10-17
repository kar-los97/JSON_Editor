package gui;

import exceptions.JSONErrorException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import lexing.Lexem;
import lexing.Lexer;
import parsing.IJSParser;
import parsing.JSParser;
import tokens.Token;
import values.JSArray;
import values.JSObject;
import values.Value;
import writing.IJSWriter;
import writing.JSWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Controller {
    JSObject object;
    @FXML
    TreeView<String> treeJS;

    @FXML
    private void initialize() throws IOException, JSONErrorException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        File f = chooser.showOpenDialog(Main.stage);
        List<Lexem> lexems = Lexer.getInstance().readLexemsFromFile(f);
        IJSParser JSReader = new JSParser();
        Queue<Token> tokens = JSReader.createTokensFromLexems(lexems);
/*        for(Token t:tokens){
            System.out.println(t);
        }*/
        object = JSReader.parseJSObject(tokens,null);

        TreeItem<String> rootItem = new TreeItem<String> ("{");
        rootItem.setExpanded(true);
        for (Value v:(List<Value>)object.getValue()) {
            if(v instanceof JSArray){
                TreeItem<String> trItem = new TreeItem<>("\""+v.getName()+"\""+": [");
                for (Value va:(List<Value>)v.getValue()) {
                    trItem.getChildren().add(new TreeItem<>(""+va.getValue()));
                }
                rootItem.getChildren().add(trItem);
            }else if (v instanceof JSObject){
                TreeItem<String> trItem = new TreeItem<>("\""+v.getName()+"\""+": {");
                for(Value va:(List<Value>)v.getValue()){
                    trItem.getChildren().add(new TreeItem<>("\""+va.getName()+"\""+": "+va.getValue()));
                }
                rootItem.getChildren().add(trItem);
            }else{
                rootItem.getChildren().add(new TreeItem<>("\""+v.getName()+"\""+": "+v.getValue()));
            }
        }
        treeJS.setRoot(rootItem);
    }

    private void createTreeObject(JSObject object, TreeItem<String> item){

    }

    public void saveJSON(ActionEvent actionEvent) throws IOException {
        if(object!=null){
            IJSWriter jsWriter = new JSWriter();
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
            File file = chooser.showSaveDialog(Main.stage);
            jsWriter.writeJSObject(object,file);
        }
    }
}
