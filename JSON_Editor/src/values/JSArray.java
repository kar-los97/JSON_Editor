package values;

import java.util.ArrayList;
import java.util.List;

public class JSArray extends Value {
    private List<Value> values;
    public JSArray() {
        values = new ArrayList<>();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    void setValue(Object value) {

    }
}
