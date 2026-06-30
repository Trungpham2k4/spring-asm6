package fa.training.asm6.exception;

import fa.training.asm6.exception.custom.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e) {
        return "error/404";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFound(UsernameNotFoundException e) {
        return "error/404";
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public String handleUnAuthorized(Exception e) {
        return "error/403";
    }

//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception e) {
//        return "error/400";
//    }
}
