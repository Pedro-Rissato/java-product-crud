package br.com.rissato.exception;

public class DuplicateProductException extends DatabaseException {
    public DuplicateProductException(String message, Throwable cause) {
        super(message, cause);
    }
}