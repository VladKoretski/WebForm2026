import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestWebForm {

    WebDriver driver;
    // Порт берётся из системы, по умолчанию 9999 (как в application.conf)
    private static final String BASE_URL = System.getProperty("app.url", "http://localhost:9999/");

    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
        System.out.println("🔧 Testing URL: " + BASE_URL);
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void testPageLoads() {
        // Простейший тест: проверяем, что страница загрузилась
        String title = driver.getTitle();
        System.out.println("✅ Page title: " + title);
        Assertions.assertNotNull(title);
    }

    @Test
    void testEmptyFields() {
        String expectedResult = "Поле обязательно для заполнения";
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[4]/button")).click();
        
        String actualResult = driver.findElement(
            By.xpath("//*[@id=\"root\"]/div/form/div[1]/span/span/span[3]")
        ).getText().trim();
        
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testHappyPath() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        String expectedResult = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        
        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("[data-test-id=order-success]")
        ));
        
        String actualResult = driver.findElement(
            By.cssSelector("[data-test-id=order-success]")
        ).getText().trim();
        
        Assertions.assertEquals(expectedResult, actualResult);
    }
}
