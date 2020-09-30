package values;

import exceptions.JSONErrorException;
import values.Value;

import java.util.ArrayList;
import java.util.List;

public class JSArray extends Value<List<Value>>{
    private List<Value> values;

    public JSArray() {
        values = new ArrayList<>();
    }

    public JSArray(String name, List<Value> values) {
        super(name);
        this.values = values;
    }

    @Override
    public List<Value> getValue() {
        return values;
    }

    @Override
    public void setValue(List<Value> value) throws JSONErrorException {
        values = value;
    }
}
