package br.com.rissato;

import br.com.rissato.config.ConnectionFactory;
import br.com.rissato.config.DbConfig;
import br.com.rissato.controller.ProductController;
import br.com.rissato.repository.ProductRepository;
import br.com.rissato.repository.ProductRepositoryPostgres;
import br.com.rissato.service.ProductService;
import br.com.rissato.view.ProductView;

public class Main {
    public static void main(String[] args) {
        DbConfig dbConfig = DbConfig.load();
        ConnectionFactory connectionFactory = new ConnectionFactory(dbConfig);
        try {
            ProductRepository productRepository =
                    new ProductRepositoryPostgres(connectionFactory.getDataSource());

            ProductService productService = new ProductService(productRepository);
            ProductController productController = new ProductController(productService);
            ProductView productView = new ProductView(productController);

            productView.start();
        } finally {
            connectionFactory.shutdown();
        }
    }
} 