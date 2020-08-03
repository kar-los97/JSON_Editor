package values;

public class StringValue extends Value {
    private String value;

    public StringValue(String value) {
        this.value = value;
    }

    public StringValue() {
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String)value;
    }
}
