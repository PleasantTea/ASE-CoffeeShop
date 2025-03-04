package exception;

public class InvalidOrdersFileReadException extends Exception{
	// Constructor - accepts only error messages
    public InvalidOrdersFileReadException(String message) {
        super(message);
    }

    // Constructor - accepts both the error message and the cause of the exception
    public InvalidOrdersFileReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
