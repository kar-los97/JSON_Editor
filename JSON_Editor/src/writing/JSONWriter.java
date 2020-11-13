package writing;

import converting.IJSONConverter;
import converting.JSONConverter;
import exceptions.JSONErrorException;
import values.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSONWriter implements IJSONWriter{

    @Override
    public void writeJSONToFile(JSONObject JSONObject, File fileToWrite) throws IOException, JSONErrorException {
        IJSONConverter JSONConverter = new JSONConverter();
        String JSONinString = JSONConverter.convertJSON(JSONObject);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
        writer.write(JSONinString);
        writer.close();
    }
}
