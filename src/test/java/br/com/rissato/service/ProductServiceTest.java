package br.com.rissato.service;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.rissato.dto.ProductPriceResponseDTO;
import br.com.rissato.dto.ProductRequestDTO;
import br.com.rissato.dto.ProductResponseDTO;
import br.com.rissato.exception.ProductNotFoundException;
import br.com.rissato.exception.ValidationException;
import br.com.rissato.repository.ProductRepository;
import br.com.rissato.repository.ProductRepositoryInMemory;

public class ProductServiceTest {
    private ProductService service;
    private ProductRepository repository;

    @BeforeEach
    public void setup () {
        repository = new ProductRepositoryInMemory();
        service = new ProductService(repository);
        
    }
    
    //createProduct tests
    @Test
    public void shouldCreateProductWithValidDTO() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
         service.createProduct(dto);
         

         assertEquals(1, repository.findAll().size(), "Product should be created with valid DTO");
            assertEquals("Test Product", repository.findAll().get(0).getName(), "Product name should match");
            assertEquals(new BigDecimal("10.99"), repository.findAll().get(0).getPrice(), "Product price should match");
            assertEquals(100, repository.findAll().get(0).getStock(), "Product stock should match");
            assertEquals("Test Description", repository.findAll().get(0).getDescription(), "Product description should match");
    }

@Test
public void shouldThrowValidationExceptionWhenCreatingProductWithNullDTO() {

    ValidationException ex = assertThrows(ValidationException.class, () -> service.createProduct(null));
    assertEquals("Product data cannot be null.", ex.getMessage());
    assertTrue(repository.findAll().isEmpty(), "Product should not be created with null DTO");

    }

    @Test
    public void shouldThrowValidationExceptionWhenCreatingProductWithNullName() {
        ProductRequestDTO dto = new ProductRequestDTO(
            null,
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );

         ValidationException ex = assertThrows(ValidationException.class, () -> service.createProduct(dto));
         assertEquals("Name cannot be null or empty.", ex.getMessage());
         assertTrue(repository.findAll().isEmpty(), "Product should not be created with null name");   
    }

    @Test
    public void shouldThrowValidationExceptionWhenCreatingProductWithBlankName() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "   ",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        
        ValidationException ex = assertThrows(ValidationException.class, () -> service.createProduct(dto));
        assertEquals("Name cannot be null or empty.", ex.getMessage());
        assertTrue(repository.findAll().isEmpty(), "Product should not be created with blank name");
        
    }

    @Test
    public void shouldThrowValidationExceptionWhenCreatingProductWithNullPrice() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            null,
            100,
            "Test Description"
        );
        ValidationException ex = assertThrows(ValidationException.class, () -> service.createProduct(dto));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertTrue(repository.findAll().isEmpty(), "Product should not be created with null price");
    }
    @Test
    public void shouldThrowValidationExceptionWhenCreatingProductWithZeroPrice() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            BigDecimal.ZERO,
            100,
            "Test Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.createProduct(dto));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertTrue(repository.findAll().isEmpty(), "Product should not be created with zero price");
    }