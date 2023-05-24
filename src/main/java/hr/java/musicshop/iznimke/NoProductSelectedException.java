package hr.java.musicshop.iznimke;

public class NoProductSelectedException extends Exception{

    public NoProductSelectedException() {
    }

    public NoProductSelectedException(String message) {
        super(message);
    }

    public NoProductSelectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoProductSelectedException(Throwable cause) {
        super(cause);
    }
}
