package br.com.meli.matchsaver.exceptions;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String entityName) {
        super(entityName + " not found");
    }
}
