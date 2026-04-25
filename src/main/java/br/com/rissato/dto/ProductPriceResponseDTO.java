package br.com.rissato.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.rissato.model.Product;

public record ProductPriceResponseDTO(
    Long id,
    String name,
    BigDecimal originalPrice,
    BigDecimal discountPercentage,
    BigDecimal finalPrice
) {
    public static ProductPriceResponseDTO from(Product product) {
        BigDecimal discountPercentage = product.getDiscountPercentage();

        if (discountPercentage == null) {
            discountPercentage = BigDecimal.ZERO;
        }

        BigDecimal finalPrice = product.getPrice();

        if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = product.getPrice()
                    .multiply(discountPercentage)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            finalPrice = product.getPrice()
                    .subtract(discount)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return new ProductPriceResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            discountPercentage,
            finalPrice
        );
    }
}