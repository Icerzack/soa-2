package com.soa.second.soa_second.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.soa.second.soa_second.controller.NavigatorController;
import com.soa.second.soa_second.dto.ErrorDefaultDTO;
import com.soa.second.soa_second.exception.EntityNotFoundException;
import com.soa.second.soa_second.exception.NotValidParamsException;
import com.soa.second.soa_second.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = {NavigatorController.class})
public class ProcessExceptionHandler {
    @ExceptionHandler({InvalidFormatException.class})
    public ResponseEntity<?> handleInvalidFormatException(Exception e) {
        System.out.println(e.getMessage());
        int code = 400;
        String message = "Невалидный формат";
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.status(code).body(errorDTO);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
        System.out.println(e.getMessage());
        int code = 404;
        String message = "Сущность не найдена";
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.status(code).body(errorDTO);
    }

    @ExceptionHandler({DateTimeParseException.class})
    public ResponseEntity<?> handleDateTimeParseException(DateTimeParseException e) {
        System.out.println(e.getMessage());
        int code = 422;
        String message = "Ошибка с парсингом даты, " + e.getMessage();
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.status(code).body(errorDTO);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(Exception e) {
        System.out.println(e.getMessage());
        int code = 422;
        String message = "Невалидный формат json";
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.status(code).body(errorDTO);
    }


    @ExceptionHandler({ConstraintViolationException.class,
            BindException.class,
            MethodArgumentNotValidException.class,
            InvocationTargetException.class,
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class,
            NotValidParamsException.class,
            NumberFormatException.class,
            ValidationException.class,
            MethodArgumentTypeMismatchException.class,
    })
    public ResponseEntity<?> handleValidationException(Exception e) {
        System.out.println(e.getMessage() + " " + e.getClass().getCanonicalName());
        int code = HttpStatus.BAD_REQUEST.value();
        String message = "Некорректные входные параметры: " + e.getMessage();
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDefaultDTO> handleNoHandlerFoundException(ResourceNotFoundException e) {
        System.out.println(e.getMessage() + " " + e.getClass().getCanonicalName());
        int code = HttpStatus.BAD_REQUEST.value();
        String message = "Несуществующий URI";
        ZonedDateTime currentDateTime = ZonedDateTime.now(java.time.ZoneId.of("UTC+3"));
        String formattedCurrentDateTime = currentDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        ErrorDefaultDTO errorDTO = new ErrorDefaultDTO();
        errorDTO.setCode(code);
        errorDTO.setMessage(message);
        errorDTO.setTime(formattedCurrentDateTime);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(errorDTO);
    }
}
