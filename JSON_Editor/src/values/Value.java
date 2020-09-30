package values;

import exceptions.JSONErrorException;

public abstract class Value<T> {
    private String name;
    public String getName(){
        return name;
    }
    public Value(String name){
        this.name = name;
    }

    public Value(){

    }
    public abstract T getValue();
    public abstract void setValue(T value) throws JSONErrorException;
}
