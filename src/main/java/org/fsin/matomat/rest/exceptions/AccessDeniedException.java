package org.fsin.matomat.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason="access denied")
public class AccessDeniedException  extends RuntimeException {

}
