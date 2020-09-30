package values;

public class StringValue extends Value<String> {
    private String value;

    public StringValue(String name,String value) {
        super(name);
        this.value = value;
    }

    public StringValue() {
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "\""+this.getName()+"\""+" : "+ "\""+value+"\"";
    }
}
