package writing;

import converting.IJSONConverter;
import converting.JSONConverter;
import exceptions.JSONErrorException;
import values.JSObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter implements IJSONWriter{

    @Override
    public void writeJSONToFile(JSObject JSONObject, File fileToWrite) throws IOException, JSONErrorException {
        IJSONConverter JSONConverter = new JSONConverter();
        String JSONinString = JSONConverter.convertJSON(JSONObject);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
        writer.write(JSONinString);
        writer.close();
    }
}
