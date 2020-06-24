package bg.bbanchev.challenges.tset.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import bg.bbanchev.challenges.tset.service.NewerVersionAlreadyReleasedException;
import bg.bbanchev.challenges.tset.service.VersionAlreadyExistsException;

/**
 * Handle problems with the API.
 * 
 * @author Borislav Banchev
 *
 */
@ControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

	// raise error in case of wrong payload (optional)
	@ExceptionHandler(value = { IllegalArgumentException.class })
	protected ResponseEntity<Object> handleBadRequests(RuntimeException ex, WebRequest request) {
		String response = ex.getMessage();
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	// instead of create we return OK response for already deployed service
	@ExceptionHandler(value = { VersionAlreadyExistsException.class })
	protected ResponseEntity<Object> handleConflict(VersionAlreadyExistsException ex, WebRequest request) {
		String response = String.valueOf(ex.getSystemVersionNumber());
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.OK, request);
	}

	// raise conflict in case of deployment of previous version (optional)
	@ExceptionHandler(value = { NewerVersionAlreadyReleasedException.class })
	protected ResponseEntity<Object> handleConflict(NewerVersionAlreadyReleasedException ex, WebRequest request) {
		String response = ex.getMessage();
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
}