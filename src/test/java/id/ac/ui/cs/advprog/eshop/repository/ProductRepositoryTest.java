package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
    }

    @Test
    void create_shouldGenerateId() {
        Product product = new Product();
        product.setProductName("Sampo");
        product.setProductQuantity(1);

        productRepository.create(product);

        Product saved = productRepository.findAll().next();
        assertNotNull(saved.getProductId(), "Created product should have ID");
    }

    @Test
    void create_shouldSaveName() {
        Product product = new Product();
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);

        productRepository.create(product);

        Product saved = productRepository.findAll().next();
        assertEquals("Sampo Cap Bambang", saved.getProductName(), "Name should be saved");
    }

    @Test
    void create_shouldSaveQuantity() {
        Product product = new Product();
        product.setProductName("Sampo");
        product.setProductQuantity(100);

        productRepository.create(product);

        Product saved = productRepository.findAll().next();
        assertEquals(100, saved.getProductQuantity(), "Quantity should be saved");
    }

    @Test
    void findAll_emptyRepository() {
        assertFalse(productRepository.findAll().hasNext(), "Empty repo should return no data");
    }

    @Test
    void update_existingProduct_shouldChangeName() {
        Product product = new Product();
        product.setProductName("Old");
        product.setProductQuantity(1);
        productRepository.create(product);

        UUID id = product.getProductId();

        Product updated = new Product();
        updated.setProductId(id);
        updated.setProductName("New");
        updated.setProductQuantity(1);

        productRepository.update(updated);

        assertEquals("New", productRepository.findById(id).getProductName(), "Name should update");
    }

    @Test
    void delete_existingProduct_shouldRemove() {
        Product product = new Product();
        product.setProductName("Delete");
        product.setProductQuantity(1);
        productRepository.create(product);

        productRepository.deleteById(product.getProductId());

        assertFalse(productRepository.findAll().hasNext(), "Product should be deleted");
    }

    @Test
    void findById_notFound_shouldReturnNull() {
        assertNull(productRepository.findById(UUID.randomUUID()), "Unknown ID should return null");
    }

    @Test
    void testFindByIdNotFound() {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Sampo Cap Bambang");
        productRepository.create(product);
        Product foundProduct = productRepository.findById(UUID.randomUUID());
        assertNull(foundProduct);
    }

    @Test
    void testUpdateNotFound() {
        Product product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Sampo Cap Bambang");
        productRepository.create(product);

        Product nonExistentProduct = new Product();
        nonExistentProduct.setProductId(UUID.randomUUID());
        nonExistentProduct.setProductName("Produk Ilegal");

        productRepository.update(nonExistentProduct);
        Product originalProduct = productRepository.findById(product.getProductId());
        assertEquals("Sampo Cap Bambang", originalProduct.getProductName());
    }
}


