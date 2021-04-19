package values;

public class JSONNullValue extends JSONValue {
    public JSONNullValue(String name) {
        super(name);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public String toString() {
        return "null";
    }
}
