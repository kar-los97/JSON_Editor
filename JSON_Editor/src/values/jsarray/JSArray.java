package values.jsarray;

import values.Value;

import java.util.ArrayList;
import java.util.List;

public class JSArray extends Value {
    private List<JSArrayValue> values;
    public JSArray() {
        values = new ArrayList<>();
    }

    public JSArray(String name, List<JSArrayValue> values) {
        super(name);
        this.values = values;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }
}
