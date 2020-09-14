package values.jsarray;

import exceptions.JSONErrorException;
import values.Value;

import java.util.ArrayList;
import java.util.List;

public class JSArray extends Value {
    private List<Value> values;
    public JSArray() {
        values = new ArrayList<>();
    }

    public JSArray(String name, List<Value> values) {
        super(name);
        this.values = values;
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public void setValue(Object value) throws JSONErrorException {
        if(value instanceof List && ((List<Value>) value).get(0) instanceof Value){
            values = (List<Value>)value;
        }else{
            throw new JSONErrorException("List of values from array expected!");
        }
    }
}
