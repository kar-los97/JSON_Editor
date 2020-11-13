package values;



import exceptions.JSONErrorException;

import java.util.ArrayList;
import java.util.List;

public class JSONObject extends JSONValue<List<JSONValue>> {
    private List<JSONValue> JSONValues;

    public JSONObject(List<JSONValue> JSONValues) {
        this.JSONValues = JSONValues;
    }

    public JSONObject() {
        JSONValues = new ArrayList<>();
    }

    public JSONObject(String name){
        super(name);
        JSONValues = new ArrayList<>();
    }

    public void addValue(JSONValue val) throws JSONErrorException {
        if(checkName(val)){
            throw new JSONErrorException("The name \""+val.getName()+"\" exist in this object.");
        }
        JSONValues.add(val);
    }

    @Override
    public List<JSONValue> getValue() {
        return JSONValues;
    }

    @Override
    public void setValue(List<JSONValue> JSONValue) {

    }

    private boolean checkName(JSONValue newJSONValue){
        if(newJSONValue instanceof JSONObject){
            return false;
        }
        for (JSONValue v: JSONValues) {
            if(v.getName().equals(newJSONValue.getName())){
                return true;
            }
        }
        return false;
    }
}
