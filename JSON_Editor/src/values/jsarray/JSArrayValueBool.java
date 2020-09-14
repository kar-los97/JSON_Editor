package values.jsarray;

import exceptions.JSONErrorException;

public class JSArrayValueBool extends JSArrayValue{
    private boolean value;

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    void setValue(Object value) throws JSONErrorException {
        if(value instanceof Boolean) {
            this.value = (Boolean) value;
        }else{
            throw new JSONErrorException("Boolean expected!");
        }
    }
}
