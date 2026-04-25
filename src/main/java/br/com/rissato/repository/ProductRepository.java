package br.com.rissato.repository;

import java.util.List;
import java.util.Optional;

import br.com.rissato.model.Product;

public interface ProductRepository{
    void save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void update(Product product);
    void updateStockTransactional(Long id, int quantity);
    void deleteById(Long id);
}
      