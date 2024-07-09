package pl.aplazuk.homewrok7news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class NewsExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException .class)
    public ResponseEntity<List<String>> handleValidationErrors(MethodArgumentNotValidException validException){
        List<String> errors = validException.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
