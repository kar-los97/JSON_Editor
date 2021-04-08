package converting;

import exceptions.JSONErrorException;
import values.JSONObject;

public interface IJSONConverter {
    String convertJSON(JSONObject object) throws JSONErrorException;
}
