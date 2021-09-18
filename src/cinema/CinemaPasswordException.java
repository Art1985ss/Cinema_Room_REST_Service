package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CinemaPasswordException extends RuntimeException {
    public CinemaPasswordException(String message) {
        super(message);
    }
}
