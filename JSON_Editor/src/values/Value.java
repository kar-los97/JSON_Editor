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
    public abstract Object getValue();
    public abstract void setValue(Object value);
}
