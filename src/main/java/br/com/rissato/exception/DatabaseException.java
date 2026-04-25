package br.com.rissato.exception;

public class DatabaseException extends ProductCrudException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}