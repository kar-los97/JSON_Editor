package converting;

import exceptions.JSONErrorException;
import values.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONConverter implements IJSONConverter {
    private int numberOfTabs;
    private String buffer;

    public JSONConverter(){
        numberOfTabs = 0;
        buffer = "";
    }

    private boolean isValueListValid(List<Value> list){
        return list!=null&&!list.isEmpty();
    }

    private void addTabs(StringBuilder JSONStringBuilder) throws IOException {
        if(numberOfTabs>=0){
            for (int i = 0; i < numberOfTabs; i++) {
                JSONStringBuilder.append("\t");
            }
        }
    }

    private boolean isStringValue(Value value){
        return value instanceof StringValue;
    }

    private  boolean isNumberValue(Value value){
        return value instanceof NumberValue;
    }

    private boolean isNullValue(Value value){
        return value instanceof NullValue;
    }

    private boolean isJSArray(Value value){
        return value instanceof JSArray;
    }

    private boolean isJSObject(Value value){
        return value instanceof JSObject;
    }

    private boolean isBoolValue(Value value){
        return value instanceof  BoolValue;
    }

    @Override
    public String convertJSON(JSObject object) throws IOException, JSONErrorException {
        StringBuilder JSONStringBuilder = new StringBuilder();
        numberOfTabs = 0;
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(object, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n}");
        return JSONStringBuilder.toString();
    }

    private void convertJSValue(Value value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        writeStringValue(value.getName(),JSONStringBuilder);
        JSONStringBuilder.append(": ");
        writeValue(value,JSONStringBuilder);
    }

    private void convertNestedJSObject(JSObject value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(value, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("}");
    }

    private void writeJSObjectValues(JSObject value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        for (int i = 0; i < value.getValue().size() - 1; i++) {
            convertJSValue(value.getValue().get(i), JSONStringBuilder);
            JSONStringBuilder.append(",");
        }
        if(isValueListValid(value.getValue())) {
            convertJSValue(value.getValue().get(value.getValue().size() - 1), JSONStringBuilder);
        }
    }

    private void writeJSArray(JSArray array, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
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

    private void writeValue(Value value, StringBuilder JSONStringBuilder) throws IOException, JSONErrorException {
        if(isJSArray(value)){
            writeJSArray((JSArray) value,JSONStringBuilder);
        }else if(isJSObject(value)){
            convertNestedJSObject((JSObject) value, JSONStringBuilder);
        }else if(isNumberValue(value)){
            writeNumberValue(((NumberValue)value).getValue(),JSONStringBuilder);
        }else if(isStringValue(value)){
            writeStringValue(((StringValue)value).getValue(),JSONStringBuilder);
        }else if(isNullValue(value)){
            writeNullValue(JSONStringBuilder);
        }else if(isBoolValue(value)){
            writeBoolValue(((BoolValue)value).getValue(),JSONStringBuilder);
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
