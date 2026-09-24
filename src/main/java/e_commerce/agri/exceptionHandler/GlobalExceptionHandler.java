package e_commerce.agri.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?> AlreadyExistsExceptionExe(AlreadyExistsException exe) {
        ErrorResponse err = new ErrorResponse(exe.getMessage(), "FAILED");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> runtimeExe(NotFoundException exe) {
        ErrorResponse err = new ErrorResponse(exe.getMessage(), "FAILED");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

      @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exe(Exception exe) {
        ErrorResponse err = new ErrorResponse(exe.getMessage(), "FAILED");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
    }
}
