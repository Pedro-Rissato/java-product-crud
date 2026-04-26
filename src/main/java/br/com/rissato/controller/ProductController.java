package br.com.rissato.controller;

import java.math.BigDecimal;
import java.util.List;

import br.com.rissato.dto.ProductPriceResponseDTO;
import br.com.rissato.dto.ProductRequestDTO;
import br.com.rissato.dto.ProductResponseDTO;
import br.com.rissato.service.ProductService;

public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public void createProduct(ProductRequestDTO dto) {
        this.productService.createProduct(dto);
    }

    public ProductResponseDTO getProductById(Long id) {
        return this.productService.getProductById(id);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return this.productService.getAllProducts();
    }

    public void updateProduct(Long id, ProductRequestDTO dto) {
        this.productService.updateProduct(id, dto);
    }

    public void deleteById(Long id) {
        this.productService.deleteById(id);
    }

    public void updateStock(Long id, Integer quantity) {
        this.productService.updateStock(id, quantity);
    }

    public void updateProductPrice(Long id, BigDecimal price) {
        this.productService.adjustPrice(id, price);
    }

    public ProductPriceResponseDTO getProductFinalPrice(Long id) {
        return this.productService.getFinalPrice(id);
    }

    public void updateDiscount(Long id, BigDecimal discount) {
        this.productService.updateDiscount(id, discount);
    }

    public void updateDescription(Long id, String description) {
        this.productService.adjustDescription(id, description);
    }

    public void updateName(Long id, String name) {
        this.productService.adjustName(id, name);
    }
}