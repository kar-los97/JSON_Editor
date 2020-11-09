package writing;

import exceptions.JSONErrorException;
import values.JSObject;

import java.io.File;
import java.io.IOException;

public interface IJSWriter {
    void writeJSObject(JSObject object, File fileToWrite) throws IOException, JSONErrorException;
}
