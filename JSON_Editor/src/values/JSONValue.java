package values;

import exceptions.JSONErrorException;

public abstract class JSONValue<T> {
    private String name;

    public String getName() {
        return name;
    }

    public JSONValue(String name) {
        this.name = name;
    }

    public JSONValue() {
    }

    public abstract T getValue();

    public abstract void setValue(T value) throws JSONErrorException;
}
