package br.com.foursales.app.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        this(message, null);
    }

	public NotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, (short) 1001, new StringBuilder("Recurso não encontrado")
            .append(": ")
            .append(message)
            .toString(), cause);
    }
}
