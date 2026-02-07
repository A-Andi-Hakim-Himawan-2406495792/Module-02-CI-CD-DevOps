package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws Exception {
        // 1. Buka halaman create product
        driver.get(baseUrl + "/product/create");

        // 2. Isi Form
        // Mencari elemen berdasarkan ID yang digenerate oleh th:field="*{productName}"
        WebElement nameInput = driver.findElement(By.id("productName"));
        nameInput.clear();
        nameInput.sendKeys("Sampo Cap Selenium");

        // Mencari elemen berdasarkan ID yang digenerate oleh th:field="*{productQuantity}"
        WebElement quantityInput = driver.findElement(By.id("productQuantity"));
        quantityInput.clear();
        quantityInput.sendKeys("20");

        // 3. Klik Tombol Submit
        // Mencari tombol berdasarkan tipe dan class
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // 4. Verifikasi Hasil
        // Pastikan kita sudah berpindah halaman (misal ke Product List)
        // Kita cek apakah produk yang baru dibuat muncul di halaman list
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Sampo Cap Selenium"));
        assertTrue(pageSource.contains("20"));
    }
}