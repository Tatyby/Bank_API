package com.sberStart.bank_api.exception;

import com.sberStart.bank_api.dto.MessageExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BankExceptionHandler {
    @ExceptionHandler(BankException.class)
    public ResponseEntity<?> bankErrorHandler(BankException bankException) {
        String msg = "Error";
        log.info(msg + bankException.getMessage());
        return new ResponseEntity<>(new MessageExceptionDTO(bankException.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
