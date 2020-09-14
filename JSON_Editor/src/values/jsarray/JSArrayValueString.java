package values.jsarray;

import exceptions.JSONErrorException;

public class JSArrayValueString extends JSArrayValue{
    private String value;
    @Override
    public String getValue() {
        return value;
    }

    @Override
    void setValue(Object value) throws JSONErrorException {
        if(value instanceof String) {
            this.value = (String) value;
        }else{
            throw new JSONErrorException("String exception");
        }
    }
}
