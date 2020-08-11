package values;

public class BoolValue extends Value{

    private boolean value;

    public BoolValue(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public BoolValue() {
    }

    @Override
    Object getValue() {
        return null;
    }

    @Override
    void setValue(Object value) {

    }
}
