package bcs.exception;

public class IEXTradingException extends RuntimeException {

	public IEXTradingException() {
		super();
	}

	public IEXTradingException(String message) {
		super(message);
	}

	public IEXTradingException(String message, Throwable cause) {
		super(message, cause);
	}

	public IEXTradingException(Throwable cause) {
		super(cause);
	}

	protected IEXTradingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
