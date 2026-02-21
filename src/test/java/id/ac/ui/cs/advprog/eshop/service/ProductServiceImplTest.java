package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void testCreate() {
        Product product = new Product();

        Product result = service.create(product);

        assertEquals(product, result);
        Mockito.verify(repository).create(product);
    }

    @Test
    void testFindAll() {
        Product p1 = new Product();
        Product p2 = new Product();

        Iterator<Product> iterator = List.of(p1, p2).iterator();
        Mockito.when(repository.findAll()).thenReturn(iterator);

        List<Product> result = service.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Product product = new Product();

        Mockito.when(repository.findById(id)).thenReturn(product);

        Product result = service.findById(id);

        assertEquals(product, result);
    }

    @Test
    void testUpdate() {
        Product product = new Product();

        service.update(product);

        Mockito.verify(repository).update(product);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();

        service.deleteById(id);

        Mockito.verify(repository).deleteById(id);
    }
}