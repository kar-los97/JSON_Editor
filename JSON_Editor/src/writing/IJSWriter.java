package writing;

import values.JSObject;

import java.io.File;
import java.io.IOException;

public interface IJSWriter {
    void writeJSObject(JSObject object, File fileToWrite) throws IOException;
}
