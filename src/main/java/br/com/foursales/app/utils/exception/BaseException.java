package br.com.foursales.app.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException extends RuntimeException {
    protected final HttpStatus statusCode;
    protected final short internalCode;
    protected final String message;

	public BaseException(HttpStatus statusCode, short internalCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.internalCode = internalCode;
        this.message = message;
    }
}
