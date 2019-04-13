package org.fsin.matomat.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason="Entry already exists")
public class AlreadyExistsException extends RuntimeException {
}
