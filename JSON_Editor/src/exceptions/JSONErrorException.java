package exceptions;

public class JSONErrorException extends Exception {
    public JSONErrorException() {
    }

    public JSONErrorException(String message) {
        super(message);
    }
}