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
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }
}
