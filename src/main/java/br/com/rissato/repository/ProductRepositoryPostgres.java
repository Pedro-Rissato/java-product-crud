package br.com.rissato.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import br.com.rissato.exception.DatabaseException;
import br.com.rissato.exception.DuplicateProductException;
import br.com.rissato.exception.ProductNotFoundException;
import br.com.rissato.exception.ValidationException;
import br.com.rissato.model.Product;

public class ProductRepositoryPostgres implements ProductRepository {

    private final DataSource dataSource;

    public ProductRepositoryPostgres(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private RuntimeException translateSQLException(String message, SQLException e) {
        String sqlState = e.getSQLState();

        if ("23505".equals(sqlState)) {
            return new DuplicateProductException("A product already exists with the information provided.", e); 
        }

        return new DatabaseException(message, e);
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        return new Product(
                rs.getString("name"),
                rs.getLong("id"),
                rs.getBigDecimal("price"),
                rs.getInt("stock"),
                rs.getString("description"),    
                rs.getBigDecimal("discount_percentage")

        );
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products ORDER BY id ASC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Product> products = new ArrayList<>();
            while (rs.next()) {
                products.add(mapRow(rs));
            }
            return products;

        } catch (SQLException e) {
            throw translateSQLException("Error while fetching for products", e);
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw translateSQLException("Error while fetching by id.", e);
        }
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (name, price, stock, description, discount_percentage) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getDescription());
            stmt.setBigDecimal(5, product.getDiscountPercentage());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw translateSQLException("Error while saving product.", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name=?, price=?, stock=?, description=?, discount_percentage=? WHERE id=?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setBigDecimal(2, product.getPrice());
            stmt.setInt(3, product.getStock());
            stmt.setString(4, product.getDescription());
            stmt.setBigDecimal(5, product.getDiscountPercentage());
            stmt.setLong(6, product.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw translateSQLException("Error while updating the product", e);
        }
    }
    @Override
    public void updateStockTransactional(Long id, int quantity) {
        String sqlSelect = "SELECT stock FROM products WHERE id = ? FOR UPDATE";
        String sqlUpdate = "UPDATE products SET stock = ? WHERE id = ?";

        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement selectStmt = conn.prepareStatement(sqlSelect)) {
                selectStmt.setLong(1, id);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new ProductNotFoundException("Product not found: " + id);
                    }
                    int currentStock = rs.getInt("stock");
                    int newStock = currentStock + quantity;
                    if (newStock < 0) {
                        throw new ValidationException("Stock cannot be negative");
                    }

                    try (PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {
                        updateStmt.setInt(1, newStock);
                        updateStmt.setLong(2, id);
                        int rows = updateStmt.executeUpdate();
                        if (rows == 0) {
                            throw new DatabaseException("Failed to update stock", null);
                        }
                    }
                }
                conn.commit();
            } catch (RuntimeException e) {
                conn.rollback();
                throw e;
            } catch (SQLException e) {
                conn.rollback();
                throw translateSQLException("Error updating stock", e);
            }

            } catch (SQLException e) {
                throw translateSQLException("Connection error updating stock", e);
            }
    }
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw translateSQLException("Error while deleting the product.", e);
        }
    }
}