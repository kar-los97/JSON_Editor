package values.jsarray;

public class JSArrayValueNull extends JSArrayValue{
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    void setValue(Object value) {
        throw new NullPointerException();
    }
}
