package uni.social.connect.exception;

public class EmailExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4439452682974960113L;

	public EmailExistsException(String message) {
		super(message);
	}

	public EmailExistsException() {
		super();
	}
}
