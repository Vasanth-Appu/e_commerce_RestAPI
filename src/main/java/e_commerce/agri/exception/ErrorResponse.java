package e_commerce.agri.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private String errorCode;
    private int status;
    private LocalDateTime time;

    public ErrorResponse(String message, String errorCode, int status) {
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
        this.time = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTime() {
        return time;
    }
}