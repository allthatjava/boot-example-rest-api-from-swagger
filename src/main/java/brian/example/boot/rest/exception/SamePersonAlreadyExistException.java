package brian.example.boot.rest.exception;

public class SamePersonAlreadyExistException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SamePersonAlreadyExistException() {
		super();
	}
	
	public SamePersonAlreadyExistException(String message) {
		super(message);
	}
	
	public SamePersonAlreadyExistException(Throwable e, String message) {
		super(message, e );
	}
	
}
