package gr.balasis.hotel.context.web.advice;

import gr.balasis.hotel.context.base.exception.*;
import gr.balasis.hotel.context.base.exception.conflict.ReservationConflictException;
import gr.balasis.hotel.context.base.exception.corrupted.CorruptedModelException;
import gr.balasis.hotel.context.base.exception.dublicate.DuplicateException;
import gr.balasis.hotel.context.base.exception.notfound.*;
import gr.balasis.hotel.context.base.exception.unauthorized.UnauthorizedAccessException;
import gr.balasis.hotel.context.web.validation.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HotelException.class)
    public ResponseEntity<String> handleHotelException(HotelException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ReservationConflictException.class)
    public ResponseEntity<String> handleReservationConflictException(ReservationConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(CorruptedModelException.class)
    public ResponseEntity<String> handleCorruptedModelException(CorruptedModelException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<String> handleDuplicateException(DuplicateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(InvalidResourceException.class)
    public ResponseEntity<String> handleInvalidResourceException(InvalidResourceException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}