package values.jsarray;

public class JSArrayValueBool extends JSArrayValue{
    private boolean value;

    @Override
    Object getValue() {
        return value;
    }

    @Override
    void setValue(Object value) {
        this.value = (boolean)value;
    }
}
