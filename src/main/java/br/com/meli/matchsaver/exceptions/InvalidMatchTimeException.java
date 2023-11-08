package br.com.meli.matchsaver.exceptions;

public class InvalidMatchTimeException extends RuntimeException{
    public InvalidMatchTimeException(String message){
        super(message);
    }
}
