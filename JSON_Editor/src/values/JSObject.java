package values;



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

    public void addValue(Value val){
        values.add(val);
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public void setValue(Object value) {

    }
}
