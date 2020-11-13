package values;

public class JSONStringJSONValue extends JSONValue<String> {
    private String value;

    public JSONStringJSONValue(String name, String value) {
        super(name);
        this.value = value;
    }

    public JSONStringJSONValue() {
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
        return "\""+this.getName()+"\""+" : "+ "\""+value+"\"";
    }
}
