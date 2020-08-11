package values;

public abstract class Value {
    private String name;
    public String getName(){
        return name;
    }
    public Value(String name){
        this.name = name;
    }

    public Value(){

    }
    abstract Object getValue();
    abstract void setValue(Object value);
}
