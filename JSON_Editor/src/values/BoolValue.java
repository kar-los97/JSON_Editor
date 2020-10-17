package values;

public class BoolValue extends Value<Boolean>{

    private boolean value;

    public BoolValue(String name, boolean value) {
        super(name);
        this.value = value;
    }

    public BoolValue() {
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\""+this.getName()+"\""+" : "+ value;
    }
}
