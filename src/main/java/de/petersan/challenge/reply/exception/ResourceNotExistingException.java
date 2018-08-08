package de.petersan.challenge.reply.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class ResourceNotExistingException extends RuntimeException {

  private static final long serialVersionUID = -7218712556118740288L;

  public ResourceNotExistingException() {}

  public ResourceNotExistingException(String msg) {
    super(msg);
  }

}
