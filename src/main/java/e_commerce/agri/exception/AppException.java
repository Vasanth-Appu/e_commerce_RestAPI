package e_commerce.agri.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 511193895901950924L;
	
	private final String errorCode;
    private final HttpStatus status;

    public AppException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
}