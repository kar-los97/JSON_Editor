package values;

public class JSONStringValue extends JSONValue<String> {
    private String value;

    public JSONStringValue(String name, String value) {
        super(name);
        this.value = value;
    }

    public JSONStringValue() {
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
