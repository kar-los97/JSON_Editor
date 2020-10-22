package writing;

import values.JSArray;
import values.JSObject;
import values.Value;

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

    @Override
    public void writeJSObject(JSObject object, File fileToWrite) throws IOException {
        numberOfTabs = 0;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileToWrite));
        bufferedWriter.write("{");
        numberOfTabs++;
        writeJSObjectValues(object, bufferedWriter);
        bufferedWriter.close();
        numberOfTabs--;
    }

    private void writeJSValue(Value value,BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("\n");
        writeTabs(bufferedWriter);
        if(value instanceof JSArray){
            writeJSArray((JSArray) value,bufferedWriter);
        }else if (value instanceof JSObject){
            writeNestedJSObject((JSObject)value,bufferedWriter);
        }else{
            bufferedWriter.write(value.toString());
        }
    }

    private void writeNestedJSObject(JSObject value, BufferedWriter bufferedWriter) throws IOException {
        writeStringValue(value.getName(),bufferedWriter);
        bufferedWriter.write(": {\n");
        numberOfTabs++;
        writeJSObjectValues(value, bufferedWriter);
        numberOfTabs--;
    }

    private void writeJSObjectValues(JSObject value, BufferedWriter bufferedWriter) throws IOException {
        for (int i = 0; i < value.getValue().size() - 1; i++) {
            writeJSValue(value.getValue().get(i), bufferedWriter);
            bufferedWriter.write(",");
        }
        if(isValueListValid(value.getValue())) {
            writeJSValue(value.getValue().get(value.getValue().size() - 1), bufferedWriter);
        }
        bufferedWriter.write("\n}");
    }

    private void writeJSArray(JSArray array, BufferedWriter bufferedWriter) throws IOException {
        writeStringValue(array.getName(),bufferedWriter);
        bufferedWriter.write(": [\n");
        numberOfTabs++;
        /* upravit - brat v potaz - ze muze v poli byt objekt ci dalsi pole */
        for(int i = 0; i<array.getValue().size()-1;i++){
            writeTabs(bufferedWriter);
            bufferedWriter.write(array.getValue().get(i).getValue().toString());
            bufferedWriter.write(",");
            bufferedWriter.write("\n");
        }
        writeTabs(bufferedWriter);
        if(isValueListValid(array.getValue())) {
            bufferedWriter.write(array.getValue().get(array.getValue().size() - 1).getValue().toString() + "\n");
        }
        numberOfTabs--;
        writeTabs(bufferedWriter);
        bufferedWriter.write("]");

    }

    private void writeStringValue(String stringValue, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write("\""+ stringValue + "\"");
    }
}
