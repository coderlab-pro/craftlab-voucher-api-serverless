package pro.craftlab.voucher.endpoint.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pro.craftlab.voucher.endpoint.rest.model.Exception;
import pro.craftlab.voucher.repository.model.exception.*;

@RestControllerAdvice
@Slf4j
public class InternalToRestExceptionHandler {

  @ExceptionHandler(value = {BadRequestException.class})
  ResponseEntity<Exception> handleBadRequest(BadRequestException e) {
    log.info("Bad request", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {MissingServletRequestParameterException.class})
  ResponseEntity<Exception> handleBadRequest(MissingServletRequestParameterException e) {
    log.info("Missing parameter", e);
    return handleBadRequest(new BadRequestException(e.getMessage()));
  }

  @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
  ResponseEntity<Exception> handleBadRequest(HttpRequestMethodNotSupportedException e) {
    log.info("Unsupported method for this endpoint", e);
    return handleBadRequest(new BadRequestException(e.getMessage()));
  }

  @ExceptionHandler(value = {HttpMessageNotReadableException.class})
  ResponseEntity<Exception> handleBadRequest(HttpMessageNotReadableException e) {
    log.info("Missing required body", e);
    return handleBadRequest(new BadRequestException(e.getMessage()));
  }

  @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
  ResponseEntity<Exception> handleConversionFailed(MethodArgumentTypeMismatchException e) {
    log.info("Conversion failed", e);
    String message = e.getCause().getCause().getMessage();
    return handleBadRequest(new BadRequestException(message));
  }

  @ExceptionHandler(value = {TooManyRequestsException.class})
  ResponseEntity<Exception> handleTooManyRequests(TooManyRequestsException e) {
    log.info("Too many requests", e);
    return new ResponseEntity<>(
        toRest(e, HttpStatus.TOO_MANY_REQUESTS), HttpStatus.TOO_MANY_REQUESTS);
  }

  @ExceptionHandler(value = {ForbiddenException.class})
  ResponseEntity<Exception> handleDefault(ForbiddenException e) {
    log.error("Authentication error", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {NotFoundException.class})
  ResponseEntity<Exception> handleNotFound(NotFoundException e) {
    log.info("Not found", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(value = {NotImplementedException.class})
  ResponseEntity<Exception> handleNotFound(NotImplementedException e) {
    log.info("Not implemented", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.NOT_IMPLEMENTED), HttpStatus.NOT_IMPLEMENTED);
  }

  @ExceptionHandler(value = {java.lang.Exception.class})
  ResponseEntity<Exception> handleDefault(java.lang.Exception e) {
    log.error("Internal error", e);
    return new ResponseEntity<>(
        toRest(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Exception toRest(java.lang.Exception e, HttpStatus status) {
    var restException = new Exception();
    restException.setType(status.toString());
    restException.setMessage(e.getMessage());
    return restException;
  }
}
