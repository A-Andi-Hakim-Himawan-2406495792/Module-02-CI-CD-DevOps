package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        product = new Product();
        productId = UUID.fromString("eb558e9f-1c39-460e-8860-71af6af63bd6");

        product.setProductId(productId);
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals(productId, product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, product.getProductQuantity());
    }
}
