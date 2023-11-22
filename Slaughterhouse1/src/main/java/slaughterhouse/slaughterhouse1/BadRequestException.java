package slaughterhouse.slaughterhouse1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
class BadRequestException extends RuntimeException { }
