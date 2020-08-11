package values;


import java.util.ArrayList;
import java.util.List;

public class JSObject extends Value{
    private List<Value> values;

    public JSObject(List<Value> values) {
        this.values = values;
    }

    public JSObject() {
        values = new ArrayList<>();
    }

    public void addValue(Value val){
        values.add(val);
    }

    @Override
    Object getValue() {
        return null;
    }

    @Override
    void setValue(Object value) {

    }
}
