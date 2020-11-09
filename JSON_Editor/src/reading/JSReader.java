package reading;

import java.io.*;

public class JSReader implements IJSReader{
    @Override
    public String readJSONFromFile(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = bufferedReader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while(line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        return stringBuilder.toString();
    }
}
