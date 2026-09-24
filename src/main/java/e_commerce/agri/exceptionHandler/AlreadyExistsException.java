package e_commerce.agri.exceptionHandler;

public class AlreadyExistsException  extends RuntimeException {
    public AlreadyExistsException(String msg){
        super(msg);
    }
}