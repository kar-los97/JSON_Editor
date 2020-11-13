package values;

import exceptions.JSONErrorException;

import java.util.ArrayList;
import java.util.List;

public class JSONArray extends JSONValue<List<JSONValue>> {
    private List<JSONValue> JSONValues;

    public JSONArray() {
        JSONValues = new ArrayList<>();
    }

    public JSONArray(String name, List<JSONValue> JSONValues) {
        super(name);
        this.JSONValues = JSONValues;
    }

    @Override
    public List<JSONValue> getValue() {
        return JSONValues;
    }

    @Override
    public void setValue(List<JSONValue> JSONValue) throws JSONErrorException {
        JSONValues = JSONValue;
    }
}
