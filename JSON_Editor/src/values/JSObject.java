package values;



import exceptions.JSONErrorException;

import java.util.ArrayList;
import java.util.List;

public class JSObject extends Value {
    private List<Value> values;

    public JSObject(List<Value> values) {
        this.values = values;
    }

    public JSObject() {
        values = new ArrayList<>();
    }

    public JSObject(String name){
        super(name);
        values = new ArrayList<>();
    }

    public void addValue(Value val) throws JSONErrorException {
        if(checkName(val)){
            throw new JSONErrorException("The name \""+val.getName()+"\" exist in this object.");
        }
        values.add(val);
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public void setValue(Object value) {

    }

    private boolean checkName(Value newValue){
        if(newValue instanceof JSObject){
            return false;
        }
        for (Value v:values) {
            if(v.getName().equals(newValue.getName())){
                return true;
            }
        }
        return false;
    }
}
