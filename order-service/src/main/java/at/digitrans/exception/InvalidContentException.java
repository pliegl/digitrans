package at.digitrans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Generic exception used for invalid or incomplete content
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidContentException extends RuntimeException {


  public InvalidContentException(String message) {
    super(message);
  }
}
