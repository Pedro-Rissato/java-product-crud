package br.com.rissato.service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.rissato.dto.ProductPriceResponseDTO;
import br.com.rissato.dto.ProductRequestDTO;
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


    @Test
    public void shouldUpdateProductWithValidDTO() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            new BigDecimal("20.99"),
            200,
            "Updated Description"
        );

        service.updateProduct(productId, updateDTO);

        assertEquals("Updated Product", repository.findById(productId).get().getName(), "Product name should be updated");
        assertEquals(new BigDecimal("20.99"), repository.findById(productId).get().getPrice(), "Product price should be updated");
        assertEquals(200, repository.findById(productId).get().getStock(), "Product stock should be updated");
        assertEquals("Updated Description", repository.findById(productId).get().getDescription(), "Product description should be updated");
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenUpdatingNonExistingProduct() {
        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            new BigDecimal("20.99"),
            200,
            "Updated Description"
        );

        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.updateProduct(999L, updateDTO));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNullDTO() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, null));
        assertEquals("Product data cannot be null.", ex.getMessage());
        assertEquals("Test Product", repository.findById(productId).get().getName(), "Product should not be updated with null DTO");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNullName() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            null,
            new BigDecimal("20.99"),
            200,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Name cannot be null or empty.", ex.getMessage());
        assertEquals("Test Product", repository.findById(productId).get().getName(), "Product name should not be updated with null name");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithBlankName() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "   ",
            new BigDecimal("20.99"),
            200,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Name cannot be null or empty.", ex.getMessage());
        assertEquals("Test Product", repository.findById(productId).get().getName(), "Product name should not be updated with blank name");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNullPrice() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            null,
            200,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("10.99"), repository.findById(productId).get().getPrice(), "Product price should not be updated with null price");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithZeroPrice() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            BigDecimal.ZERO,
            200,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("10.99"), repository.findById(productId).get().getPrice(), "Product price should not be updated with zero price");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNegativePrice() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            new BigDecimal("-1.00"),
            200,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("10.99"), repository.findById(productId).get().getPrice(), "Product price should not be updated with negative price");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNegativeStock() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            new BigDecimal("20.99"),
            -1,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Stock cannot be null or negative.", ex.getMessage());
        assertEquals(100, repository.findById(productId).get().getStock(), "Product stock should not be updated with negative stock");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingProductWithNullStock() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductRequestDTO updateDTO = new ProductRequestDTO(
            "Updated Product",
            new BigDecimal("20.99"),
            null,
            "Updated Description"
        );

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateProduct(productId, updateDTO));
        assertEquals("Stock cannot be null or negative.", ex.getMessage());
        assertEquals(100, repository.findById(productId).get().getStock(), "Product stock should not be updated with null stock");
    }


     @Test
    public void shouldUpdateStockWithPositiveQuantity(){
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();
        service.updateStock(productId, 50);
        assertEquals(150, repository.findById(productId).get().getStock(), "Stock should be updated correctly with positive quantity");
    }
    @Test
    public void shouldUpdateStockWithNegativeQuantity(){
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productID = repository.findAll().get(0).getId();
        service.updateStock(productID,-50);
        assertEquals(50, repository.findById(productID).get().getStock(), "Stock should be updated correctly with negative quantity");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingStockWithZeroQuantity(){
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productID = repository.findAll().get(0).getId();
        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateStock(productID, 0));
        assertEquals("Quantity cannot be null or zero", ex.getMessage());
        assertEquals(100, repository.findById(productID).get().getStock(), "Stock should not be updated with zero quantity");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingStockWithNullQuantity(){
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("10.99"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productID = repository.findAll().get(0).getId();
        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateStock(productID, null));
        assertEquals("Quantity cannot be null or zero", ex.getMessage());
        assertEquals(100, repository.findById(productID).get().getStock(), "Stock should not be updated with null quantity");
    }
    @Test
    public void shouldUpdateDiscountForExistingProduct() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.updateDiscount(productId, new BigDecimal("10.00"));

        assertEquals(new BigDecimal("10.00"), repository.findById(productId).get().getDiscountPercentage(), "Product price should be updated with discount");    
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenUpdatingDiscountForNonExistingProduct() {
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.updateDiscount(999L, new BigDecimal("10.00")));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingDiscountWithNullValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateDiscount(productId, null));
        assertEquals("Discount must be between 0 and 100.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be updated with null discount");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingDiscountWithNegativeValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateDiscount(productId, new BigDecimal("-10.00")));
        assertEquals("Discount must be between 0 and 100.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be updated with negative discount");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingDiscountWithEqualTo100() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateDiscount(productId, new BigDecimal("100.00")));
        assertEquals("Discount must be between 0 and 100.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be updated with discount greater than or equal to 100");
    }
    @Test
    public void shouldThrowValidationExceptionWhenUpdatingDiscountWithValueGreaterThan100() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateDiscount(productId, new BigDecimal("110.00")));
        assertEquals("Discount must be between 0 and 100.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be updated with discount greater than or equal to 100");
    }

  
    @Test
    public void shouldCalculateFinalPriceWithDiscount() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.updateDiscount(productId, new BigDecimal("10.00"));
        ProductPriceResponseDTO priceResponse = service.getFinalPrice(productId);

        assertEquals(new BigDecimal("90.00"), priceResponse.finalPrice(), "Final price should be calculated with discount");
    }
    @Test
    public void shouldHandleNullDiscountAsZero() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ProductPriceResponseDTO priceResponse = service.getFinalPrice(productId);

        assertEquals(new BigDecimal("100.00"), priceResponse.finalPrice(), "Final price should be equal to original price when no discount is applied");
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenGettingFinalPriceForNonExistingProduct() {
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.getFinalPrice(999L));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }
    @Test
    public void shouldReturnOriginalPriceWithZeroDiscount() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.updateDiscount(productId, BigDecimal.ZERO);
        ProductPriceResponseDTO priceResponse = service.getFinalPrice(productId);

        assertEquals(new BigDecimal("100.00"), priceResponse.finalPrice(), "Final price should be equal to original price when discount is zero"); 
    }


    @Test
    public void shouldAdjustPriceWithValidValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.adjustPrice(productId, new BigDecimal("150.00"));

        assertEquals(new BigDecimal("150.00"), repository.findById(productId).get().getPrice(), "Product price should be adjusted with valid value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingPriceWithNullValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustPrice(productId, null));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be adjusted with null value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingPriceWithZeroValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustPrice(productId, BigDecimal.ZERO));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be adjusted with zero value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingPriceWithNegativeValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustPrice(productId, new BigDecimal("-50.00")));
        assertEquals("Price must be greater than zero.", ex.getMessage());
        assertEquals(new BigDecimal("100.00"), repository.findById(productId).get().getPrice(), "Product price should not be adjusted with negative value");
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenAdjustingPriceForNonExistingProduct() {
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.adjustPrice(999L, new BigDecimal("150.00")));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }


    @Test
    public void shouldAdjustNameWithValidValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.adjustName(productId, "Updated Product");

        assertEquals("Updated Product", repository.findById(productId).get().getName(), "Product name should be adjusted with valid value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingNameWithNullValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustName(productId, null));
        assertEquals("Name cannot be null or empty.", ex.getMessage());
        assertEquals("Test Product", repository.findById(productId).get().getName(), "Product name should not be adjusted with null value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingNameWithBlankValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustName(productId, "   "));
        assertEquals("Name cannot be null or empty.", ex.getMessage());
        assertEquals("Test Product", repository.findById(productId).get().getName(), "Product name should not be adjusted with blank value");
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenAdjustingNameForNonExistingProduct() {
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.adjustName(999L, "Updated Product"));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }

    
    @Test
    public void shouldAdjustDescriptionWithValidValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        service.adjustDescription(productId, "Updated Description");

        assertEquals("Updated Description", repository.findById(productId).get().getDescription(), "Product description should be adjusted with valid value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingDescriptionWithNullValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustDescription(productId, null));
        assertEquals("Description cannot be null or empty.", ex.getMessage());
        assertEquals("Test Description", repository.findById(productId).get().getDescription(), "Product description should not be adjusted with null value");
    }
    @Test
    public void shouldThrowValidationExceptionWhenAdjustingDescriptionWithBlankValue() {
        ProductRequestDTO dto = new ProductRequestDTO(
            "Test Product",
            new BigDecimal("100.00"),
            100,
            "Test Description"
        );
        service.createProduct(dto);
        Long productId = repository.findAll().get(0).getId();

        ValidationException ex = assertThrows(ValidationException.class, () -> service.adjustDescription(productId, "   "));
        assertEquals("Description cannot be null or empty.", ex.getMessage());
        assertEquals("Test Description", repository.findById(productId).get().getDescription(), "Product description should not be adjusted with blank value");
    }
    @Test
    public void shouldThrowProductNotFoundExceptionWhenAdjustingDescriptionForNonExistingProduct() {
        ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> service.adjustDescription(999L, "Updated Description"));
        assertEquals("Product not found with the id: 999.", ex.getMessage());
    }
}