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

    public JSONObject(String name) {
        super(name);
        JSONValues = new ArrayList<>();
    }

    public void addValue(JSONValue val) throws JSONErrorException {
        if (checkName(val)) {
            throw new JSONErrorException("The name \"" + val.getName() + "\" exist in this object.");
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

    private boolean checkName(JSONValue newJSONValue) {
        if (newJSONValue instanceof JSONObject) {
            return false;
        }
        for (JSONValue v : JSONValues) {
            if (v.getName().equals(newJSONValue.getName())) {
                return true;
            }
        }
        return false;
    }

/*    public JSONObject filterName(String filteredName) throws JSONErrorException {
        JSONObject newObject = new JSONObject(this.getName());
        for(JSONValue val:JSONValues){
            if(val instanceof JSONObject){
                JSONObject filteredObject =((JSONObject) val).filterName(filteredName);
                if(filteredObject!=null) newObject.addValue(filteredObject);
            }else if(val instanceof JSONArray){
                if(val.getName().contains(filteredName)){
                    newObject.addValue(val);
                }else{
                    JSONArray newArray = ((JSONArray) val).filterName(filteredName);
                    if(newArray!=null) newObject.addValue(newArray);
                }
            }else if(val.getName().contains(filteredName)){
                newObject.addValue(val);
            }
        }
        if(newObject.getValue().size()==0){
            return null;
        }else{
            return newObject;
        }

    }*/
}
