package values;

public class JSONNullJSONValue extends JSONValue {
    public JSONNullJSONValue(String name) {
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
        return "\""+this.getName()+"\""+" : "+ "null";
    }
}
