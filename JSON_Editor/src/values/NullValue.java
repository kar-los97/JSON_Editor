package values;

public class NullValue extends Value{
    public NullValue(String name) {
        super(name);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public String toString() {
        return "\""+this.getName()+"\""+" : "+ "null";
    }
}
