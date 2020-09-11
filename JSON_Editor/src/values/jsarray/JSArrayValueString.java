package values.jsarray;

public class JSArrayValueString extends JSArrayValue{
    private String value;
    @Override
    Object getValue() {
        return value;
    }

    @Override
    void setValue(Object value) {
        this.value = (String)value;
    }
}
