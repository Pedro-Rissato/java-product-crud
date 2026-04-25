package br.com.rissato.dto;

import java.math.BigDecimal;

import br.com.rissato.model.Product;

public record ProductResponseDTO(
    Long id,
    String name,
    BigDecimal price,
    Integer stock,
    String description,
    BigDecimal discountPercentage
) {
    public static ProductResponseDTO from(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getStock(),
            product.getDescription(),
            product.getDiscountPercentage()
        );
    }
}