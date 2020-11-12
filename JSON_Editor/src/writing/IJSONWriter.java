package writing;

import exceptions.JSONErrorException;
import values.JSObject;

import java.io.File;
import java.io.IOException;

public interface IJSONWriter {
    void writeJSONToFile(JSObject JSONObject, File fileToWrite) throws IOException, JSONErrorException;
}
