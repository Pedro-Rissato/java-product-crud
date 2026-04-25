package br.com.rissato.exception;

public class ProductNotFoundException extends ProductCrudException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}