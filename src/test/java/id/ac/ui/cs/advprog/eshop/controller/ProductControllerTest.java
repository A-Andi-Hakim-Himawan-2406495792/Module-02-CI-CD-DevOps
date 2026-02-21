package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    // GET /product/create
    @Test
    void testCreatePage() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("createProduct"));
    }

    // POST /product/create
    @Test
    void testCreatePost() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("name", "Indomie")
                        .param("price", "3000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        Mockito.verify(service).create(Mockito.any(Product.class));
    }

    // GET /product/list
    @Test
    void testProductList() throws Exception {
        Mockito.when(service.findAll()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("products"))
                .andExpect(view().name("productList"));
    }

    // GET /product/edit/{id} (product ada)
    @Test
    void testEditPageFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.findById(id)).thenReturn(new Product());

        mockMvc.perform(get("/product/edit/" + id))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name("editProduct"));
    }

    // GET /product/edit/{id} (product null → branch)
    @Test
    void testEditPageNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(service.findById(id)).thenReturn(null);

        mockMvc.perform(get("/product/edit/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));
    }

    // POST /product/edit
    @Test
    void testEditPost() throws Exception {
        mockMvc.perform(post("/product/edit")
                        .param("name", "Updated")
                        .param("price", "5000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        Mockito.verify(service).update(Mockito.any(Product.class));
    }

    // POST /product/delete/{id}
    @Test
    void testDelete() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(post("/product/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        Mockito.verify(service).deleteById(id);
    }
}