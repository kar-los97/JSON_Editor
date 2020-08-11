package values;


import java.util.ArrayList;
import java.util.List;

public class JSObject extends Value{
    private String name;
    private Value value;
    private List<Value> values;

    public JSObject(String name, Value value, List<Value> values) {
        this.name = name;
        this.value = value;
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
