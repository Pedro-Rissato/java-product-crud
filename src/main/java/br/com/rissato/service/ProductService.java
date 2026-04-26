package br.com.rissato.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.rissato.dto.ProductPriceResponseDTO;
import br.com.rissato.dto.ProductRequestDTO;
import br.com.rissato.dto.ProductResponseDTO;
import br.com.rissato.exception.ProductNotFoundException;
import br.com.rissato.exception.ValidationException;
import br.com.rissato.model.Product;
import br.com.rissato.repository.ProductRepository;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    private void validateProductDTO(ProductRequestDTO dto) {
    if (dto == null) {
        throw new ValidationException("Product data cannot be null.");
    }
    if (dto.name() == null || dto.name().isBlank()) {
        throw new ValidationException("Name cannot be null or empty.");
    }
    if (dto.price() == null || dto.price().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ValidationException("Price must be greater than zero.");
    }
    if (dto.stock() == null || dto.stock() < 0) {
        throw new ValidationException("Stock cannot be negative.");
    }
}

    public void createProduct(ProductRequestDTO dto) {
        validateProductDTO(dto);
        Product product = new Product(
                dto.name(),
                null,
                dto.price(),
                dto.stock(),
                dto.description()
        );
        repository.save(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll()
                .stream()
                .map(ProductResponseDTO::from)
                .toList();
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = getExistingProduct(id);
        return ProductResponseDTO.from(product);
    }

    public void updateProduct(Long id, ProductRequestDTO dto) {
        validateProductDTO(dto);

        Product exists = getExistingProduct(id);

        exists.setName(dto.name());
        exists.setDescription(dto.description());
        exists.setPrice(dto.price());
        exists.setStock(dto.stock());

        repository.update(exists);
    }

    public void deleteById(Long id) {
        getExistingProduct(id);
        repository.deleteById(id);
    }

    

    public void updateDiscount(Long id, BigDecimal discount) {
        Product exists = getExistingProduct(id);

        if (discount == null ||
                discount.compareTo(BigDecimal.ZERO) < 0 ||
                discount.compareTo(BigDecimal.valueOf(100)) >= 0) {
            throw new ValidationException("Discount must be between 0 and 100.");
        }

        exists.setDiscountPercentage(discount);
        repository.update(exists);
    }

    public ProductPriceResponseDTO getFinalPrice(Long id) {
        Product exists = getExistingProduct(id);
        return ProductPriceResponseDTO.from(exists);
    }

    public void adjustPrice(Long id, BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Price must be greater than zero.");
        }

        Product exists = getExistingProduct(id);
        exists.setPrice(price);
        repository.update(exists);
    }

    public void adjustDescription(Long id, String description) {
        if (description == null || description.isBlank()) {
            throw new ValidationException("Description cannot be null or empty.");
        }

        Product exists = getExistingProduct(id);
        exists.setDescription(description);
        repository.update(exists);
    }

    public void adjustName(Long id, String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name cannot be null or empty.");
        }

        Product exists = getExistingProduct(id);
        exists.setName(name);
        repository.update(exists);
    }

    private Product getExistingProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not find with the id: " + id));
    }
    public void updateStock(Long id, Integer quantity) {
    if (quantity == null || quantity == 0) {
        throw new ValidationException("Quantity cannot be null or zero");
    }
    repository.updateStockTransactional(id, quantity); 
    }
}