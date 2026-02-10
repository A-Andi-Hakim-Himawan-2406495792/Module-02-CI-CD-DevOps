package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product create(Product product);

    List<Product> findAll();

    Product findById(UUID id);

    void update(Product product);

    void deleteById(UUID id);
}