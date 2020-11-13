package converting;

import exceptions.JSONErrorException;
import values.JSONObject;
import java.io.IOException;

public interface IJSONConverter {
    String convertJSON(JSONObject object) throws IOException, JSONErrorException;
}
