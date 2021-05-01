package values;

import exceptions.JSONErrorException;

import java.util.ArrayList;
import java.util.List;

public class JSONArray extends JSONValue<List<JSONValue>> {
    private List<JSONValue> JSONValues;

    public JSONArray(String name) {
        super(name);
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

    public void addValue(JSONValue newValue) {
        JSONValues.add(newValue);
    }

/*    public JSONArray filterName(String filteredName) throws JSONErrorException {
        JSONArray newArray = new JSONArray(this.getName());
        for(JSONValue val:JSONValues){
            if(val instanceof JSONObject){
                JSONObject newObject = ((JSONObject) val).filterName(filteredName);
                if(newObject!=null) newArray.addValue(newObject);
            }else if(val instanceof JSONArray){
                JSONArray filteredArray = ((JSONArray)val).filterName(filteredName);
                if(filteredArray!=null) newArray.addValue(filteredArray);
            }
        }
        if(newArray.getValue().size()==0){
            return null;
        }else{
            return newArray;
        }
    }*/
}
