package com.financemanager.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import com.financemanager.controller.utils.ResponseEntityBuilder;
import com.financemanager.exception.APIException;
import com.financemanager.exception.ResourceNotFoundException;
import com.financemanager.exception.UserAlreadyExistsException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String METHOD_NOT_FOUND_INFO = "Could not find the %s method for URL %s";
    private static final String UNSUPPORTED_MEDIA_TYPE_INFO = " Media type is not supported. Supported media types are ";
    private static final String MISSING_PARAMETER_INFO = "%s parameter is missing";
    private static final String MISSING_REQUEST_PARAMETERS_ERROR = "Missing Parameters";
    private static final String CONSTRAINT_VIOLATIONS_ERROR = "Constraint Violations";
    private static final String TYPE_MISMATCH_ERROR = "Type Mismatch";
    private static final String VALIDATION_ERRORS = "Validation Errors";
    private static final String MALFORMED_JSON_ERROR = "Malformed JSON request";
    private static final String RESOURCE_NOT_FOUND_ERROR = "Resource Not Found";
    private static final String USER_ALREADY_EXISTS_ERROR = "User with current email already exists.";
    private static final String NO_ACCES_ERROR = "You dont' have rights to acces this resource";
    private static final String METHOD_NOT_FOUND_ERROR = "Method Not Found";
    private static final String UNSUPPORTED_MEDIA_TYPE_ERROR = "Unsupported Media Type";

    @ExceptionHandler({ UserAlreadyExistsException.class })
    protected ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
            WebRequest request) {
        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        APIException apiException = new APIException(USER_ALREADY_EXISTS_ERROR, HttpStatus.BAD_REQUEST,
                LocalDateTime.now(), details);
        return new ResponseEntity<>(apiException, apiException.getHttpStatus());
    }
    
    @ExceptionHandler(value = { ResourceNotFoundException.class })
	protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException(RESOURCE_NOT_FOUND_ERROR, HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException(MALFORMED_JSON_ERROR, HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getObjectName() + " : " + error.getDefaultMessage()).collect(Collectors.toList());

		APIException apiException = new APIException(VALIDATION_ERRORS, HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException(TYPE_MISMATCH_ERROR, HttpStatus.BAD_REQUEST, LocalDateTime.now(),
				details);

		return ResponseEntityBuilder.build(apiException);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(ex.getMessage());

		APIException apiException = new APIException(CONSTRAINT_VIOLATIONS_ERROR, HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(String.format(MISSING_PARAMETER_INFO, ex.getParameterName()));

		APIException apiException = new APIException(MISSING_REQUEST_PARAMETERS_ERROR, HttpStatus.BAD_REQUEST,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(UNSUPPORTED_MEDIA_TYPE_INFO);
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

		details.add(builder.toString());

		APIException apiException = new APIException(UNSUPPORTED_MEDIA_TYPE_ERROR, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		List<String> details = new ArrayList<String>();
		details.add(String.format(METHOD_NOT_FOUND_INFO, ex.getHttpMethod(), ex.getRequestURL()));

		APIException apiException = new APIException(METHOD_NOT_FOUND_ERROR, HttpStatus.NOT_FOUND,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);

	}
	
	@ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		List<String> details = new ArrayList<String>();
		details.add(NO_ACCES_ERROR);
		
		APIException apiException = new APIException(NO_ACCES_ERROR, HttpStatus.FORBIDDEN,
				LocalDateTime.now(), details);

		return ResponseEntityBuilder.build(apiException);
    }

}
