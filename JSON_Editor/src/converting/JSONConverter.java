package converting;

import exceptions.JSONErrorException;
import values.*;

import java.io.IOException;
import java.util.List;

public class JSONConverter implements IJSONConverter {
    private int numberOfTabs;
    private String buffer;

    public JSONConverter(){
        numberOfTabs = 0;
        buffer = "";
    }

    private boolean isValueListValid(List<JSONValue> list){
        return list!=null&&!list.isEmpty();
    }

    private void addTabs(StringBuilder JSONStringBuilder) throws IOException {
        if(numberOfTabs>=0){
            for (int i = 0; i < numberOfTabs; i++) {
                JSONStringBuilder.append("\t");
            }
        }
    }

    private boolean isStringValue(JSONValue JSONValue){
        return JSONValue instanceof JSONStringJSONValue;
    }

    private  boolean isNumberValue(JSONValue JSONValue){
        return JSONValue instanceof JSONNumberJSONValue;
    }

    private boolean isNullValue(JSONValue JSONValue){
        return JSONValue instanceof JSONNullJSONValue;
    }

    private boolean isJSArray(JSONValue JSONValue){
        return JSONValue instanceof JSONArray;
    }

    private boolean isJSObject(JSONValue JSONValue){
        return JSONValue instanceof JSONObject;
    }

    private boolean isBoolValue(JSONValue JSONValue){
        return JSONValue instanceof JSONBoolJSONValue;
    }

    @Override
    public String convertJSON(JSONObject object) throws IOException, JSONErrorException {
        StringBuilder JSONStringBuilder = new StringBuilder();
        numberOfTabs = 0;
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(object, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n}");
        return JSONStringBuilder.toString();
    }

    private void convertJSValue(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        writeStringValue(JSONValue.getName(),JSONStringBuilder);
        JSONStringBuilder.append(": ");
        writeValue(JSONValue,JSONStringBuilder);
    }

    private void convertNestedJSObject(JSONObject value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(value, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("}");
    }

    private void writeJSObjectValues(JSONObject value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        for (int i = 0; i < value.getValue().size() - 1; i++) {
            convertJSValue(value.getValue().get(i), JSONStringBuilder);
            JSONStringBuilder.append(",");
        }
        if(isValueListValid(value.getValue())) {
            convertJSValue(value.getValue().get(value.getValue().size() - 1), JSONStringBuilder);
        }
    }

    private void writeJSArray(JSONArray array, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        JSONStringBuilder.append("[\n");
        numberOfTabs++;
        for(int i = 0; i<array.getValue().size()-1;i++){
            addTabs(JSONStringBuilder);
            writeValue(array.getValue().get(i),JSONStringBuilder);
            JSONStringBuilder.append(",");
            JSONStringBuilder.append("\n");
        }
        addTabs(JSONStringBuilder);
        if(isValueListValid(array.getValue())) {
            writeValue(array.getValue().get(array.getValue().size()-1), JSONStringBuilder);
            JSONStringBuilder.append("\n");
        }
        numberOfTabs--;
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("]");

    }

    private void writeValue(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        if(isJSArray(JSONValue)){
            writeJSArray((JSONArray) JSONValue,JSONStringBuilder);
        }else if(isJSObject(JSONValue)){
            convertNestedJSObject((JSONObject) JSONValue, JSONStringBuilder);
        }else if(isNumberValue(JSONValue)){
            writeNumberValue(((JSONNumberJSONValue) JSONValue).getValue(),JSONStringBuilder);
        }else if(isStringValue(JSONValue)){
            writeStringValue(((JSONStringJSONValue) JSONValue).getValue(),JSONStringBuilder);
        }else if(isNullValue(JSONValue)){
            writeNullValue(JSONStringBuilder);
        }else if(isBoolValue(JSONValue)){
            writeBoolValue(((JSONBoolJSONValue) JSONValue).getValue(),JSONStringBuilder);
        }
        else{
            throw new JSONErrorException("Writing error, Unexpected type of value in JSON file.");
        }
    }

    private void writeBoolValue(Boolean value, StringBuilder JSONStringBuilder) throws IOException {
        JSONStringBuilder.append(value.toString());
    }

    private void writeStringValue(String stringValue, StringBuilder JSONStringBuilder) throws IOException {
        JSONStringBuilder.append("\""+ stringValue + "\"");
    }

    private void writeNumberValue(Double numberValue, StringBuilder JSONStringBuilder)throws  IOException{
        JSONStringBuilder.append(numberValue.toString());
    }

    private void writeNullValue(StringBuilder JSONStringBuilder) throws IOException {
        JSONStringBuilder.append("null");
    }
}
