package converting;

import exceptions.JSONErrorException;
import values.*;

import java.util.List;
import java.util.Map;

public class JSONConverter implements IJSONConverter {
    private int numberOfTabs;
    private String buffer;

    public JSONConverter() {
        numberOfTabs = 0;
        buffer = "";
    }

    private boolean isValueListValid(Map<String, JSONValue> values) {
        return values != null && !values.isEmpty();
    }

    private boolean isValueListValid(List<JSONValue> values) {
        return values != null && !values.isEmpty();
    }

    private void addTabs(StringBuilder JSONStringBuilder) {
        if (numberOfTabs >= 0) {
            for (int i = 0; i < numberOfTabs; i++) {
                JSONStringBuilder.append("\t");
            }
        }
    }

    private boolean isStringValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONStringJSONValue;
    }

    private boolean isNumberValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONNumberJSONValue;
    }

    private boolean isNullValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONNullJSONValue;
    }

    private boolean isJSArray(JSONValue JSONValue) {
        return JSONValue instanceof JSONArray;
    }

    private boolean isJSObject(JSONValue JSONValue) {
        return JSONValue instanceof JSONObject;
    }

    private boolean isBoolValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONBoolJSONValue;
    }

    @Override
    public String convertJSON(JSONObject object) throws JSONErrorException {
        StringBuilder JSONStringBuilder = new StringBuilder();
        numberOfTabs = 0;
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(object, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n}");
        return JSONStringBuilder.toString();
    }

    private void convertJSValue(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        writeStringValue(JSONValue.getName(), JSONStringBuilder);
        JSONStringBuilder.append(": ");
        writeValue(JSONValue, JSONStringBuilder);
    }

    private void convertNestedJSObject(JSONObject value, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("{");
        numberOfTabs++;
        writeJSObjectValues(value, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("}");
    }

    private void writeJSObjectValues(JSONObject value, StringBuilder JSONStringBuilder) throws JSONErrorException {
        for (JSONValue val : value.getValue()) {
            convertJSValue(val, JSONStringBuilder);
            JSONStringBuilder.append(",");
        }
        if (!value.getValue().isEmpty()) {
            JSONStringBuilder.deleteCharAt(JSONStringBuilder.lastIndexOf(","));
        }
    }

    private void writeJSArray(JSONArray array, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("[\n");
        numberOfTabs++;
        for (JSONValue val : array.getValue()) {
            addTabs(JSONStringBuilder);
            writeValue(val, JSONStringBuilder);
            JSONStringBuilder.append(",");
            JSONStringBuilder.append("\n");
        }
        if (!array.getValue().isEmpty()) {
            JSONStringBuilder.deleteCharAt(JSONStringBuilder.lastIndexOf(",\n"));
        }
        numberOfTabs--;
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("]");

    }

    private void writeValue(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws JSONErrorException {
        if (isJSArray(JSONValue)) {
            writeJSArray((JSONArray) JSONValue, JSONStringBuilder);
        } else if (isJSObject(JSONValue)) {
            convertNestedJSObject((JSONObject) JSONValue, JSONStringBuilder);
        } else if (isNumberValue(JSONValue)) {
            writeNumberValue(((JSONNumberJSONValue) JSONValue).getValue(), JSONStringBuilder);
        } else if (isStringValue(JSONValue)) {
            writeStringValue(((JSONStringJSONValue) JSONValue).getValue(), JSONStringBuilder);
        } else if (isNullValue(JSONValue)) {
            writeNullValue(JSONStringBuilder);
        } else if (isBoolValue(JSONValue)) {
            writeBoolValue(((JSONBoolJSONValue) JSONValue).getValue(), JSONStringBuilder);
        } else {
            throw new JSONErrorException("Writing error, Unexpected type of value in JSON file.");
        }
    }

    private void writeBoolValue(Boolean value, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append(value.toString());
    }

    private void writeStringValue(String stringValue, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append("\"" + stringValue + "\"");
    }

    private void writeNumberValue(Double numberValue, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append(numberValue.toString());
    }

    private void writeNullValue(StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append("null");
    }
}
