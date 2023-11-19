package com.example.routes.handler;

import com.example.routes.controller.RouteController;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.example.routes.dto.ErrorDefaultDTO;
import com.example.routes.exception.EntityNotFoundException;
import com.example.routes.exception.NotValidParamsException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = {RouteController.class})
public class ProcessExceptionHandler {
    private final ErrorDefaultDTO ErrorDefaultDTO = new ErrorDefaultDTO();

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(400)
                .body(ErrorDefaultDTO.setMessage("Произошла ошибка: " + e));
    }

    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<?> handleInvalidFormatException(InvalidFormatException e) {
        return ResponseEntity.status(400)
                .body(ErrorDefaultDTO.setMessage("Произошла ошибка: " + e.getMessage()));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404)
                .body(ErrorDefaultDTO.setMessage("Произошла ошибка: " + e.getMessage()));
    }

    @ExceptionHandler({NotValidParamsException.class})
    public ResponseEntity<?> handleNotValidParamsException(NotValidParamsException e) {
        return ResponseEntity.status(422)
                .body(ErrorDefaultDTO.setMessage("Произошла ошибка: " + e.getMessage()));
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(422)
                .body(ErrorDefaultDTO.setMessage("Произошла ошибка: " + e.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(422)
                .body(ErrorDefaultDTO.setMessage("Невалидные формат json"));
    }


    @ExceptionHandler({ConstraintViolationException.class, BindException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleValidationException(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            message = "Некорректные параметры " + getValidationError((MethodArgumentNotValidException) ex);
        }
        return ResponseEntity.badRequest()
                .body(ErrorDefaultDTO.setMessage(message));
    }

    private String getValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(";"));
    }
}
