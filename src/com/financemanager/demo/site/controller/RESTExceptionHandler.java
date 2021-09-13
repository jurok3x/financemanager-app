package com.financemanager.demo.site.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.financemanager.demo.site.exception.APIException;
import com.financemanager.demo.site.exception.ResourceNotFoundException;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException("Resource Not Found", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException("Malformed JSON request", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getObjectName() + " : " + error.getDefaultMessage()).collect(Collectors.toList());

		APIException apiException = new APIException("Validation Errors", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException("Type Mismatch", HttpStatus.BAD_REQUEST, LocalDateTime.now(),
				details);

		return ResponseEntityBuilder.build(apiException);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException("Constraint Violations", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getParameterName() + " parameter is missing");

		APIException apiException = new APIException("Missing Parameters", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		details.add(builder.toString());

		APIException apiException = new APIException("Unsupported Media Type", HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));

		APIException apiException = new APIException("Method Not Found", HttpStatus.NOT_FOUND,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);

	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getLocalizedMessage());

		APIException apiException = new APIException("Error occurred", HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}
}
