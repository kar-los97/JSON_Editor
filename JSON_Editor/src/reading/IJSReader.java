package reading;

import java.io.File;
import java.io.IOException;

public interface IJSReader {
    String readJSONFromFile(File file) throws IOException;
}
