package values;

public class NumberValue extends Value<Double> {
    private double value;

    public NumberValue(String name, double value) {
        super(name);
        this.value = value;
    }

    public NumberValue() {
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\""+this.getName()+"\""+" : "+ value;
    }
}
