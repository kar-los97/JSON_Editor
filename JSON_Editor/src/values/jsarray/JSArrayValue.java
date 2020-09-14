package values.jsarray;

import exceptions.JSONErrorException;

public abstract class JSArrayValue {
    public abstract Object getValue();
    abstract void setValue(Object value) throws JSONErrorException;
}
