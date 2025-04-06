package learn.solar.data;

import java.io.IOException;

public class DataException extends RuntimeException {
    public DataException(String message, IOException ex) {
        super(message);
    }
}
