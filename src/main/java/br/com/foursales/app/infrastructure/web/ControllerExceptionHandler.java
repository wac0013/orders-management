package br.com.foursales.app.infrastructure.web;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.foursales.app.application.dto.ExceptionHandlerResponse;
import br.com.foursales.app.utils.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler  {

	private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionHandlerResponse> handleGenericException(Exception ex, HttpServletRequest request) {
		logger.error("Ocorreu uma exceção não tratada: ", ex);

		var response = ExceptionHandlerResponse.builder()
			.message(ex.getMessage())
			.httpCode((short) HttpStatus.INTERNAL_SERVER_ERROR.value())
			.errorCode((short) 1100)
			.timestamp(new Date())
			.path(request.getRequestURI())
			.build();

		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ExceptionHandlerResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex, HttpServletRequest request) {
		logger.error("Autorização negada: ", ex);

		var response = ExceptionHandlerResponse.builder()
			.message(ex.getMessage())
			.httpCode((short) HttpStatus.FORBIDDEN.value())
			.errorCode((short) 1101)
			.timestamp(new Date())
			.path(request.getRequestURI())
			.build();

		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}


	@ExceptionHandler(ServletRequestBindingException.class)
	public ResponseEntity<ExceptionHandlerResponse> handleCustomException(ServletRequestBindingException ex, HttpServletRequest request) {
		logger.error("Ocorreu uma exceção não tratada: ", ex);

		var response = ExceptionHandlerResponse.builder()
			.message(ex.getMessage())
			.httpCode((short) ex.getStatusCode().value())
			.errorCode((short) 1102)
			.timestamp(new Date())
			.path(request.getRequestURI())
			.build();

		return new ResponseEntity<>(response, ex.getStatusCode());
	}


	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ExceptionHandlerResponse> handleCustomException(AuthenticationException ex, HttpServletRequest request) {
		logger.error("Ocorreu uma exceção não tratada: ", ex);

		var response = ExceptionHandlerResponse.builder()
			.message(ex.getMessage())
			.httpCode((short) HttpStatus.UNAUTHORIZED.value())
			.errorCode((short) 1103)
			.timestamp(new Date())
			.path(request.getRequestURI())
			.build();

		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ExceptionHandlerResponse> handleCustomException(BaseException ex, HttpServletRequest request) {
		logger.error("Ocorreu uma exceção não tratada: ", ex);

		var response = ExceptionHandlerResponse.builder()
			.message(ex.getMessage())
			.httpCode((short) ex.getStatusCode().value())
			.errorCode(ex.getInternalCode())
			.timestamp(new Date())
			.path(request.getRequestURI())
			.build();

		return new ResponseEntity<>(response, ex.getStatusCode());
	}

}
