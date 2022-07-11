package com.soulcode.servicos.Controllers.Exceptions;

import com.soulcode.servicos.Services.Exceptions.DataIntegrityViolationException;
import com.soulcode.servicos.Services.Exceptions.EntityNoteFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNoteFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNoteFoundException e, HttpServletRequest request){

        StandardError erro = new StandardError();
        erro.setTimestamp(Instant.now());
        erro.setStatus(HttpStatus.NOT_FOUND.value());
        erro.setError("Registro não encontrado");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());
        erro.setTrace("EntityNoteFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(
            DataIntegrityViolationException e,
            HttpServletRequest request){

        StandardError erro = new StandardError();
        erro.setTimestamp(Instant.now());
        erro.setStatus(HttpStatus.CONFLICT.value());
        erro.setError("Atributo não pode ser duplicado");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());
        erro.setTrace("DataIntegrityViolationException");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}
