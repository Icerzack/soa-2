package com.soa.second.soa_second.handler;

import com.soa.second.soa_second.controller.NavigatorController;
import com.soa.second.soa_second.dto.ErrorDefaultDTO;
import com.soa.second.soa_second.exception.EntityNotFoundException;
import com.soa.second.soa_second.exception.NotValidParamsException;
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
@ControllerAdvice(assignableTypes = {NavigatorController.class})
public class ProcessExceptionHandler {
    private final ErrorDefaultDTO errorMessage = new ErrorDefaultDTO();

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(400)
                .body(errorMessage.setMessage(e.getMessage()));
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(404)
                .body(errorMessage.setMessage(e.getMessage()));
    }

    @ExceptionHandler({NotValidParamsException.class})
    public ResponseEntity<?> handleNotValidParamsException(NotValidParamsException e) {
        return ResponseEntity.status(400)
                .body(errorMessage.setMessage(e.getMessage()));
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.status(422)
                .body(errorMessage.setMessage(e.getMessage()));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(422)
                .body(errorMessage.setMessage(e.getMessage()));
    }


    @ExceptionHandler({ConstraintViolationException.class, BindException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleValidationException(Exception ex) {
        String message = ex.getMessage();
        if (ex instanceof MethodArgumentNotValidException) {
            message = "Некорректные параметры " + getValidationError((MethodArgumentNotValidException) ex);
        }
        return ResponseEntity.badRequest()
                .body(errorMessage.setMessage(message));
    }

    private String getValidationError(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(";"));
    }
}
