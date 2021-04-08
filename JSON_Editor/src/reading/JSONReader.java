package reading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONReader implements IJSONReader {
    @Override
    public String readJSONFromFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}
