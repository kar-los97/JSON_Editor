package values.jsarray;

import exceptions.JSONErrorException;

public class JSArrayValueNumber extends JSArrayValue{
    private double value;

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    void setValue(Object value) throws JSONErrorException {
        if(value instanceof Double){
            this.value = (Double)value;
        }else{
            throw new JSONErrorException("Double expected!");
        }


    }
}
