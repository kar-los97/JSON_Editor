package writing;

import exceptions.JSONErrorException;
import values.JSONObject;

import java.io.File;
import java.io.IOException;

public interface IJSONWriter {
    File writeJSONToFile(JSONObject JSONObject, File fileToWrite) throws IOException, JSONErrorException;
}
