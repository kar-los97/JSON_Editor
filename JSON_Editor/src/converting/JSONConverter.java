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
        return JSONValue instanceof JSONStringValue;
    }

    private boolean isNumberValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONNumberValue;
    }

    private boolean isNullValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONNullValue;
    }

    private boolean isJSArray(JSONValue JSONValue) {
        return JSONValue instanceof JSONArray;
    }

    private boolean isJSObject(JSONValue JSONValue) {
        return JSONValue instanceof JSONObject;
    }

    private boolean isBoolValue(JSONValue JSONValue) {
        return JSONValue instanceof JSONBoolValue;
    }

    @Override
    public String convertJSON(JSONObject object) throws JSONErrorException {
        StringBuilder JSONStringBuilder = new StringBuilder();
        numberOfTabs = 0;
        JSONStringBuilder.append("{");
        numberOfTabs++;
        convertJSONObjectValues(object, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n}");
        return JSONStringBuilder.toString();
    }

    private void convertJSONValueWithName(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        convertJSONStringValue(JSONValue.getName(), JSONStringBuilder);
        JSONStringBuilder.append(": ");
        convertJSONValue(JSONValue, JSONStringBuilder);
    }

    private void convertNestedJSONObject(JSONObject value, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("{");
        numberOfTabs++;
        convertJSONObjectValues(value, JSONStringBuilder);
        numberOfTabs--;
        JSONStringBuilder.append("\n");
        addTabs(JSONStringBuilder);
        JSONStringBuilder.append("}");
    }

    private void convertJSONObjectValues(JSONObject value, StringBuilder JSONStringBuilder) throws JSONErrorException {
        for (JSONValue val : value.getValue()) {
            convertJSONValueWithName(val, JSONStringBuilder);
            JSONStringBuilder.append(",");
        }
        if (!value.getValue().isEmpty()) {
            JSONStringBuilder.deleteCharAt(JSONStringBuilder.lastIndexOf(","));
        }
    }

    private void convertJSONArray(JSONArray array, StringBuilder JSONStringBuilder) throws JSONErrorException {
        JSONStringBuilder.append("[\n");
        numberOfTabs++;
        for (JSONValue val : array.getValue()) {
            addTabs(JSONStringBuilder);
            convertJSONValue(val, JSONStringBuilder);
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

    private void convertJSONValue(JSONValue JSONValue, StringBuilder JSONStringBuilder) throws JSONErrorException {
        if (isJSArray(JSONValue)) {
            convertJSONArray((JSONArray) JSONValue, JSONStringBuilder);
        } else if (isJSObject(JSONValue)) {
            convertNestedJSONObject((JSONObject) JSONValue, JSONStringBuilder);
        } else if (isNumberValue(JSONValue)) {
            convertJSONNumberValue(((JSONNumberValue) JSONValue).getValue(), JSONStringBuilder);
        } else if (isStringValue(JSONValue)) {
            convertJSONStringValue(((JSONStringValue) JSONValue).getValue(), JSONStringBuilder);
        } else if (isNullValue(JSONValue)) {
            convertJSONNullValue(JSONStringBuilder);
        } else if (isBoolValue(JSONValue)) {
            convertJSONBoolValue(((JSONBoolValue) JSONValue).getValue(), JSONStringBuilder);
        } else {
            throw new JSONErrorException("Writing error, Unexpected type of value in JSON file.");
        }
    }

    private void convertJSONBoolValue(Boolean value, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append(value.toString());
    }

    private void convertJSONStringValue(String stringValue, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append("\"" + stringValue + "\"");
    }

    private void convertJSONNumberValue(Double numberValue, StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append(numberValue.toString());
    }

    private void convertJSONNullValue(StringBuilder JSONStringBuilder) {
        JSONStringBuilder.append("null");
    }
}
