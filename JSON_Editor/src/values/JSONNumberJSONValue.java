package values;

public class JSONNumberJSONValue extends JSONValue<Double> {
    private double value;

    public JSONNumberJSONValue(String name, double value) {
        super(name);
        this.value = value;
    }

    public JSONNumberJSONValue() {
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
