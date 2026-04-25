package br.com.rissato.model;

import java.math.BigDecimal;

public class Product {

    private String name;
    private Long id;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    public Product(String name, Long id, BigDecimal price, Integer stock) {
        this(name, id, price, stock, null);
    }

    public Product(String name, Long id, BigDecimal price, Integer stock, String description) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
    public Product(String name, Long id, BigDecimal price, Integer stock, String description, BigDecimal discountPercentage) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.discountPercentage = discountPercentage;
    }

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public Integer getStock() {
        return stock;
    }
    public String getDescription() {
        return description;
    }
    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }


   public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStock(Integer quantity) {
        this.stock = quantity;
    }
    public void setDescription(String description) {
        this.description = description;
    }
   


    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return java.util.Objects.equals(id, product.id);

    }
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
    


}
