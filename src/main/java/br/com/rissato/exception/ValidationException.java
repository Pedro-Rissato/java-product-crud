package br.com.rissato.exception;

public class ValidationException extends ProductCrudException {
    public ValidationException(String message) {
        super(message);
    }
}