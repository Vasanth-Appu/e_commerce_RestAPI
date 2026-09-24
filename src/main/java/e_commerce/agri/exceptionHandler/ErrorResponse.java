package e_commerce.agri.exceptionHandler;

public class ErrorResponse {

    private String message;
    private String status;

    public ErrorResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
