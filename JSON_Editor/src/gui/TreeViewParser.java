package gui;

import converting.IJSONConverter;
import converting.JSONConverter;
import exceptions.JSONErrorException;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import values.JSONArray;
import values.JSONObject;
import values.JSONValue;

import java.util.List;

public class TreeViewParser {
    private static TreeViewParser instance = null;

    public static TreeViewParser getInstance() {
        if(instance==null){
            instance = new TreeViewParser();
        }
        return instance;
    }

    public TreeView<String> loadJSONToTreeView(TreeView<String> treeJS, JSONObject jsonObject) {
        treeJS.setRoot(new TreeItem<>());
        TreeItem<String> rootItem = new TreeItem<> ("{");
        rootItem.setExpanded(true);

        addTreeItems(jsonObject, rootItem, true);

        rootItem.getChildren().add(new TreeItem<>("}"));
        treeJS.setRoot(rootItem);
        IJSONConverter JSONConverter = new JSONConverter();
        try {
            JSONConverter.convertJSON(jsonObject);
        }catch (JSONErrorException ex){
            Alerts.getInstance().showAlert("JSON converting error","JSON File is not valid",ex.getMessage(), Alert.AlertType.ERROR);
        }
        return treeJS;
    }

    private void addTreeItems(JSONValue valueToAdd, TreeItem<String> treeItem, boolean printName) {
        for (JSONValue value : (List<JSONValue>) valueToAdd.getValue()) {
            addTreeItem(treeItem, value, printName);
        }
    }

    private void addTreeItem(TreeItem<String> rootItem, JSONValue valueToAdd, boolean printName) {
        TreeItem<String> trItem;
        if (valueToAdd instanceof JSONArray) {
            addJsonArray(rootItem, valueToAdd,printName);
        } else if (valueToAdd instanceof JSONObject) {
            addJsonObject(rootItem, valueToAdd,printName);
        } else if (printName) {
            trItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": " + valueToAdd.toString());
            trItem.setExpanded(true);
            rootItem.getChildren().add(trItem);
        } else {
            trItem = new TreeItem<>("" + valueToAdd.toString());
            trItem.setExpanded(true);
            rootItem.getChildren().add(trItem);
        }
    }

    private void addJsonArray(TreeItem<String> rootItem, JSONValue valueToAdd,boolean printName) {
        TreeItem<String> treeItem;
        if(printName){
            treeItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": [");
        }else{
            treeItem = new TreeItem<>("[");
        }
        treeItem.setExpanded(true);

        addTreeItems(valueToAdd, treeItem, false);

        rootItem.getChildren().add(treeItem);
        rootItem.getChildren().add(new TreeItem<>("]"));

    }

    private void addJsonObject(TreeItem<String> rootItem, JSONValue valueToAdd,boolean printName) {
        TreeItem<String> treeItem;
        if(printName){
            treeItem = new TreeItem<>("\"" + valueToAdd.getName() + "\"" + ": {");
        }else{
            treeItem = new TreeItem<>("{");
        }
        treeItem.setExpanded(true);

        addTreeItems(valueToAdd, treeItem, true);

        rootItem.getChildren().add(treeItem);
        rootItem.getChildren().add(new TreeItem<>("}"));
    }

}
