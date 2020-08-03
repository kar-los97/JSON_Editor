package values;

public class NumberValue extends Value {
    private double value;

    public NumberValue(double value) {
        this.value = value;
    }

    public NumberValue() {
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (double) value;
    }
}
