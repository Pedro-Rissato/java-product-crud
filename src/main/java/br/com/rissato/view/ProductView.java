package br.com.rissato.view;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import br.com.rissato.controller.ProductController;
import br.com.rissato.dto.ProductPriceResponseDTO;
import br.com.rissato.dto.ProductRequestDTO;
import br.com.rissato.dto.ProductResponseDTO;
import br.com.rissato.exception.DatabaseException;
import br.com.rissato.exception.DuplicateProductException;
import br.com.rissato.exception.ProductNotFoundException;
import br.com.rissato.exception.ValidationException;

public class ProductView {

    private final ProductController productController;
    private final Scanner sc = new Scanner(System.in);

    public ProductView(ProductController productController) {
        this.productController = productController;
    }

    private void executeSafely(Runnable action) {
        try {
            action.run();
        } catch (ValidationException e) {
            System.out.println("Invalid entry: " + e.getMessage());
        } catch (ProductNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DuplicateProductException e) {
            System.out.println("Data conflict: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println("Error while accessing the database. Try again: " + e.getMessage());
        }
    }

    private void clearBuffer() {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

    private void handleInputMismatch() {
        clearBuffer();
        System.out.println("Invalid input type.");
    }

    public void createProduct() {
        try {
            clearBuffer();

            System.out.println("Enter the product name: ");
            String name = sc.nextLine();

            System.out.println("Enter the product price: ");
            BigDecimal price = sc.nextBigDecimal();

            System.out.println("Enter the product quantity: ");
            Integer quantity = sc.nextInt();
            clearBuffer();

            System.out.println("Enter the product description: ");
            String description = sc.nextLine();

            executeSafely(() -> {
                ProductRequestDTO dto = new ProductRequestDTO(name, price, quantity, description);
                productController.createProduct(dto);
                System.out.println("Product created successfully!");
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    public void showProducts() {
        int option;

        do {
            separator();
            System.out.println("Choose an option: ");
            System.out.println("1. Show all products");
            System.out.println("2. Show product by id");
            System.out.println("3. Show product final price by id");
            System.out.println("0. Exit");

            try {
                option = sc.nextInt();
                clearBuffer();

                switch (option) {
                    case 1 -> {
                        separator();
                        showAllProducts();
                    }
                    case 2 -> {
                        separator();
                        showProductById();
                    }
                    case 3 -> {
                        separator();
                        showProductFinalPrice();
                    }
                    case 0 -> {
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                handleInputMismatch();
                option = -1;
            }
        } while (option != 0);
    }

    public void showAllProducts() {
        executeSafely(() -> {
            List<ProductResponseDTO> products = productController.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("No products found.");
                return;
            }

            for (ProductResponseDTO p : products) {
                 System.out.println(p.toFormattedString());
                System.out.println("-----------------------");
            }
        });
    }

    public void showProductById() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            executeSafely(() -> {
                ProductResponseDTO product = productController.getProductById(id);
                System.out.println(product);
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    public void showProductFinalPrice() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            executeSafely(() -> {
                ProductPriceResponseDTO priceDto = productController.getProductFinalPrice(id);

                System.out.println("Product: " + priceDto.name());
                System.out.println("Original price: " + priceDto.originalPrice());
                System.out.println("Discount: " + priceDto.discountPercentage() + "%");
                System.out.println("Final price: " + priceDto.finalPrice());
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    public void updateProduct() {
        int option;

        do {
            separator();
            System.out.println("Choose an option: ");
            System.out.println("1. Update product");
            System.out.println("2. Update stock");
            System.out.println("3. Update product price");
            System.out.println("4. Update product discount");
            System.out.println("5. Update product name");
            System.out.println("6. Update product description");
            System.out.println("0. Exit");

            try {
                option = sc.nextInt();
                clearBuffer();

                switch (option) {
                    case 1 -> {
                        separator();
                        updateFullProduct();
                    }
                    case 2 -> {
                        separator();
                        updateStock();
                    }
                    case 3 -> {
                        separator();
                        updateProductPrice();
                    }
                    case 4 -> {
                        separator();
                        updateDiscount();
                    }
                    case 5 -> {
                        separator();
                        updateName();
                    }
                    case 6 -> {
                        separator();
                        updateDescription();
                    }
                    case 0 -> {
                    }
                    default -> System.out.println("Invalid option.");
                }
            } catch (InputMismatchException e) {
                handleInputMismatch();
                option = -1;
            }
        } while (option != 0);
    }

    private void updateFullProduct() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            System.out.println("Enter the product name: ");
            String name = sc.nextLine();

            System.out.println("Enter the product price: ");
            BigDecimal price = sc.nextBigDecimal();

            System.out.println("Enter the product quantity: ");
            Integer quantity = sc.nextInt();
            clearBuffer();

            System.out.println("Enter the product description: ");
            String description = sc.nextLine();

            executeSafely(() -> {
                ProductRequestDTO dto = new ProductRequestDTO(name, price, quantity, description);
                productController.updateProduct(id, dto);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void updateStock() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();

            System.out.println("Enter the amount: ");
            Integer amount = sc.nextInt();
            clearBuffer();

            executeSafely(() -> {
                productController.updateStock(id, amount);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void updateProductPrice() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            System.out.println("Enter the product new price: ");
            BigDecimal newPrice = sc.nextBigDecimal();

            executeSafely(() -> {
                productController.updateProductPrice(id, newPrice);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void updateDiscount() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            System.out.println("Enter the product discount: ");
            BigDecimal newDiscount = sc.nextBigDecimal();

            executeSafely(() -> {
                productController.updateDiscount(id, newDiscount);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void updateName() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            System.out.println("Enter the product name: ");
            String newName = sc.nextLine();

            executeSafely(() -> {
                productController.updateName(id, newName);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    private void updateDescription() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            System.out.println("Enter the product description: ");
            String newDescription = sc.nextLine();

            executeSafely(() -> {
                productController.updateDescription(id, newDescription);
                System.out.println("Product updated successfully!");
                System.out.println(productController.getProductById(id));
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    public void deleteProductById() {
        try {
            System.out.println("Enter the product ID: ");
            Long id = sc.nextLong();
            clearBuffer();

            executeSafely(() -> {
                productController.deleteById(id);
                System.out.println("Product deleted successfully!");
            });
        } catch (InputMismatchException e) {
            handleInputMismatch();
        }
    }

    public void start() {
        int option;

        do {
            separator();
            System.out.println("Choose an option");
            System.out.println("1 - Create Product");
            System.out.println("2 - Show Products");
            System.out.println("3 - Update Product");
            System.out.println("4 - Delete Product");
            System.out.println("0 - Exit");

            try {
                option = sc.nextInt();

                switch (option) {
                    case 1 -> {
                        separator();
                        createProduct();
                    }
                    case 2 -> {
                        separator();
                        showProducts();
                    }
                    case 3 -> {
                        separator();
                        updateProduct();
                    }
                    case 4 -> {
                        separator();
                        deleteProductById();
                    }
                    case 0 -> System.out.println("Exiting...");
                    default -> {
                        separator();
                        System.out.println("Invalid option.");
                    }
                }
            } catch (InputMismatchException e) {
                handleInputMismatch();
                option = -1;
            }
        } while (option != 0);

        sc.close();
    }

    public void separator() {
        System.out.println("--------------------------------");
    }
}