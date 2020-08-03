package values;

public abstract class Value {
    private String name;
    public String getName(){
        return name;
    }
    abstract Object getValue();
    abstract void setValue(Object value);
}
