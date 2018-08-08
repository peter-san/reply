package de.petersan.challenge.reply.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public final class InconsistentDataException extends RuntimeException {
  private static final long serialVersionUID = 6680466958290206605L;

  public InconsistentDataException(String message) {
    super(message);
  }

}
