package writifilmůng;

import values.JSObject;

import java.io.File;

public interface IJSWriter {
    void writeJSObject(JSObject object, File fileToWrite);
}
