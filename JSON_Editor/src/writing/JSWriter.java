package writing;

import exceptions.JSONErrorException;
import values.*;

import javax.xml.bind.ValidationEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSWriter implements IJSWriter {
    private int numberOfTabs;
    private String buffer;

    public JSWriter(){
        numberOfTabs = 0;
        buffer = "";
    }

    private boolean isValueListValid(List<Value> list){
        return list!=null&&!list.isEmpty();
    }

    private void writeTabs(BufferedWriter writer) throws IOException {
        if(numberOfTabs>=0){
            for (int i = 0; i < numberOfTabs; i++) {
                writer.write("\t");
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
    public void writeJSObject(JSObject object, File fileToWrite) throws IOException, JSONErrorException {
        numberOfTabs = 0;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWrite));
        bufferedWriter.write("{");
        numberOfTabs++;
        writeJSObjectValues(object, bufferedWriter);
        numberOfTabs--;
        bufferedWriter.write("\n}");
        bufferedWriter.close();
    }

    private void writeJSValue(Value value,BufferedWriter bufferedWriter) throws IOException, JSONErrorException {
        bufferedWriter.write("\n");
        writeTabs(bufferedWriter);
        writeStringValue(value.getName(),bufferedWriter);
        bufferedWriter.write(": ");
        writeValue(value,bufferedWriter);
    }

    private void writeNestedJSObject(JSObject value, BufferedWriter bufferedWriter) throws IOException, JSONErrorException {
        bufferedWriter.write("{");
        numberOfTabs++;
        writeJSObjectValues(value, bufferedWriter);
        numberOfTabs--;
        bufferedWriter.write("\n");
        writeTabs(bufferedWriter);
        bufferedWriter.write("}");
    }

    private void writeJSObjectValues(JSObject value, BufferedWriter bufferedWriter) throws IOException, JSONErrorException {
        for (int i = 0; i < value.getValue().size() - 1; i++) {
            writeJSValue(value.getValue().get(i), bufferedWriter);
            bufferedWriter.write(",");
        }
        if(isValueListValid(value.getValue())) {
            writeJSValue(value.getValue().get(value.getValue().size() - 1), bufferedWriter);
        }
    }

    private void writeJSArray(JSArray array, BufferedWriter bufferedWriter) throws IOException, JSONErrorException {
        bufferedWriter.write("[\n");
        numberOfTabs++;
        for(int i = 0; i<array.getValue().size()-1;i++){
            writeTabs(bufferedWriter);
            writeValue(array.getValue().get(i),bufferedWriter);
            bufferedWriter.write(",");
            bufferedWriter.write("\n");
        }
        writeTabs(bufferedWriter);
        if(isValueListValid(array.getValue())) {
            writeValue(array.getValue().get(array.getValue().size()-1), bufferedWriter);
            bufferedWriter.write("\n");
        }
        numberOfTabs--;
        writeTabs(bufferedWriter);
        bufferedWriter.write("]");

    }

    private void writeValue(Value value, BufferedWriter bufferedWriter) throws IOException, JSONErrorException {
        if(isJSArray(value)){
            writeJSArray((JSArray) value,bufferedWriter);
        }else if(isJSObject(value)){
            writeNestedJSObject((JSObject) value, bufferedWriter);
        }else if(isNumberValue(value)){
            writeNumberValue(((NumberValue)value).getValue(),bufferedWriter);
        }else if(isStringValue(value)){
            writeStringValue(((StringValue)value).getValue(),bufferedWriter);
        }else if(isNullValue(value)){
            writeNullValue(bufferedWriter);
        }else if(isBoolValue(value)){
            writeBoolValue(((BoolValue)value).getValue(),bufferedWriter);
        }
        else{
            throw new JSONErrorException("Writing error, Unexpected type of value in JSON file.");
        }
    }

    private void writeBoolValue(Boolean value, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(value.toString());
    }

    private void writeStringValue(String stringValue, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("\""+ stringValue + "\"");
    }

    private void writeNumberValue(Double numberValue, BufferedWriter bufferedWriter)throws  IOException{
        bufferedWriter.write(numberValue.toString());
    }

    private void writeNullValue(BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("null");
    }
}
