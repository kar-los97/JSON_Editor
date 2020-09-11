package values.jsarray;

public class JSArrayValueNumber extends JSArrayValue{
    private double value;

    @Override
    Object getValue() {
        return value;
    }

    @Override
    void setValue(Object value) {
        this.value = (double)value;
    }
}
