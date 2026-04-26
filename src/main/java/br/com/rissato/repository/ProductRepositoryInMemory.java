package br.com.rissato.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import br.com.rissato.model.Product;

public class ProductRepositoryInMemory implements ProductRepository {

    private final Map<Long, Product> storage = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public void save(Product product) {
        Long id = nextId++;

        Product productNew = new Product(
            product.getName(),
            id,
            product.getPrice(),
            product.getStock(),
            product.getDescription()
        );

        storage.put(id, productNew);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void update(Product product) {
        if (!storage.containsKey(product.getId())) {
            throw new NoSuchElementException("Product not found with id: " + product.getId());
        }
        storage.put(product.getId(), product);
    }

    @Override
    public void updateStockTransactional(Long id, int stock) {
        Product existing = storage.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        Integer newStock = existing.getStock() + stock;
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        Product updatedProduct = new Product(
            existing.getName(),
            existing.getId(),
            existing.getPrice(),
            newStock,
            existing.getDescription()
        );
        storage.put(id, updatedProduct);
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}