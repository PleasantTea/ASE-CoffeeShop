package exception;

public class InvalidMenuFileReadException extends Exception {
	// Constructor - accepts only error messages
    public InvalidMenuFileReadException(String message) {
        super(message);
    }

    // Constructor - accepts both the error message and the cause of the exception
    public InvalidMenuFileReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
