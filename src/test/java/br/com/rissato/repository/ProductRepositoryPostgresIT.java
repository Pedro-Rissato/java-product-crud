package br.com.rissato.repository;

import java.math.BigDecimal;
import java.sql.DriverManager;

import org.flywaydb.core.Flyway;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.com.rissato.config.ConnectionFactory;
import br.com.rissato.config.DbConfig;
import br.com.rissato.model.Product;

@Testcontainers
class ProductRepositoryPostgresIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    private ProductRepository repository;

   @BeforeAll
    public static void initDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .load();
        flyway.migrate();
    }

        @BeforeEach
    public void setUp() {
        try (var conn = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword())) {
            conn.createStatement().execute("TRUNCATE TABLE products RESTART IDENTITY");
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to clean database", e);
        }

        DbConfig config = new DbConfig(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        var connectionFactory = new ConnectionFactory(config);
        repository = new ProductRepositoryPostgres(connectionFactory.getDataSource());
    }
    @Test
    void shouldSaveAndFindProduct() {
        Product product = new Product("Mouse", null, new BigDecimal("50.00"), 10, "Mouse Gamer");
        repository.save(product);
        
        var found = repository.findById(product.getId());
        assertTrue(found.isPresent());
        assertEquals("Mouse", found.get().getName());
        assertEquals(new BigDecimal("50.00"), found.get().getPrice());
        assertEquals(10, found.get().getStock());
        assertEquals("Mouse Gamer", found.get().getDescription());
    }
    @Test
    void shouldReturnEmptyOptionalWhenFindingNonExistingProduct() {
        var found = repository.findById(999L);
        assertTrue(found.isEmpty());
    }
    @Test
    void shouldFindAllProducts() {
        Product product1 = new Product("Mouse", null, new BigDecimal("50.00"), 10, "Mouse Gamer");
        Product product2 = new Product("Keyboard", null, new BigDecimal("100.00"), 5, "Keyboard Gamer");
        repository.save(product1);
        repository.save(product2);

        var products = repository.findAll();
        assertEquals(2, products.size());
    }
    @Test
    void shouldDeleteProduct() {
        Product product = new Product("Mouse", null, new BigDecimal("50.00"), 10, "Mouse Gamer");
        repository.save(product);
        Long productId = product.getId();

        repository.deleteById(productId);
        var found = repository.findById(productId);
        assertTrue(found.isEmpty());
    }
    @Test
    void shouldUpdateProduct() {
        Product product = new Product("Mouse", null, new BigDecimal("50.00"), 10, "Mouse Gamer");
        repository.save(product);
        Long productId = product.getId();

        Product updatedProduct = new Product("Mouse Updated", productId, new BigDecimal("60.00"), 15, "Mouse Gamer Updated");
        repository.update(updatedProduct);

        var found = repository.findById(productId);
        assertTrue(found.isPresent());
        assertEquals("Mouse Updated", found.get().getName());
        assertEquals(new BigDecimal("60.00"), found.get().getPrice());
        assertEquals(15, found.get().getStock());
        assertEquals("Mouse Gamer Updated", found.get().getDescription());
    }
    @Test
    void shouldUpdateStock() {
        Product product = new Product("Mouse", null, new BigDecimal("50.00"), 10, "Mouse Gamer");
        repository.save(product);
        Long productId = product.getId();

        repository.updateStockTransactional(productId, 20);

        var found = repository.findById(productId);
        assertTrue(found.isPresent());
        assertEquals(20, found.get().getStock());
    }
}