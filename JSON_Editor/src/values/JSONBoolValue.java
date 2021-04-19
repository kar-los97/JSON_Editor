package values;

public class JSONBoolValue extends JSONValue<Boolean> {

    private boolean value;

    public JSONBoolValue(String name, boolean value) {
        super(name);
        this.value = value;
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
        return "" + value;
    }
}