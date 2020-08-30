package values;

public class NullValue extends Value{
    public NullValue(String name) {
        super(name);
    }

    @Override
    Object getValue() {
        return null;
    }

    @Override
    void setValue(Object value) {

    }
}
