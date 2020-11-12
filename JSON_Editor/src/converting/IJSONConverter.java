package converting;

import exceptions.JSONErrorException;
import values.JSObject;
import java.io.IOException;

public interface IJSONConverter {
    String convertJSON(JSObject object) throws IOException, JSONErrorException;
}
