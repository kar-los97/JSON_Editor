package values;

public class JSONNumberValue extends JSONValue<Double> {
    private double value;

    public JSONNumberValue(String name, double value) {
        super(name);
        this.value = value;
    }

    public JSONNumberValue() {
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
        return "" + value;
    }
}
