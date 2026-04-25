package br.com.rissato.exception;

public class ProductCrudException extends RuntimeException {
    public ProductCrudException(String message) {
        super(message);
    }

    public ProductCrudException(String message, Throwable cause) {
        super(message, cause);
    }
}