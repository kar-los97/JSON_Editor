package values;

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
        return null;
    }

    @Override
    void setValue(Object value) {

    }
}
