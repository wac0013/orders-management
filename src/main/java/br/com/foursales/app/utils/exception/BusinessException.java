package br.com.foursales.app.utils.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        this(message, null);
    }

	public BusinessException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, (short) 1002, new StringBuilder("Recurso não encontrado")
            .append(": ")
            .append(message)
            .toString(), cause);
    }
}
