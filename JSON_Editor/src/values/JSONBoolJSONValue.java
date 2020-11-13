package values;

public class JSONBoolJSONValue extends JSONValue<Boolean> {

    private boolean value;

    public JSONBoolJSONValue(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public JSONBoolJSONValue() {
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\""+this.getName()+"\""+" : "+ value;
    }
}
