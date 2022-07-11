package com.soulcode.servicos.Services.Exceptions;

public class DataIntegrityViolationException extends RuntimeException{

    public DataIntegrityViolationException (String mds){
        super(mds);
    }
}
