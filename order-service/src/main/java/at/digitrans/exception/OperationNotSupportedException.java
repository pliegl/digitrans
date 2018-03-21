package at.digitrans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class OperationNotSupportedException extends RuntimeException{

  public OperationNotSupportedException(String message) {
    super(message);
  }
}
