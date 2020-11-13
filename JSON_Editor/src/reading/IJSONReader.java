package reading;

import java.io.File;
import java.io.IOException;

public interface IJSONReader {
    String readJSONFromFile(File file) throws IOException;
}
