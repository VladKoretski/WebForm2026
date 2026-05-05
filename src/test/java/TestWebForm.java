import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;  // ✅ Добавлен импорт
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.PageLoadStrategy;

import java.time.Duration;

public class TestWebForm {

    WebDriver driver;
    private static String baseUrl = "http://localhost:9999/";

    @BeforeEach
void openSite(){
    System.out.println("🔍 Opening URL: " + baseUrl);
    
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");  // Классический headless (более стабильный)
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-gpu");
    options.addArguments("--window-size=1920,1080");
    options.addArguments("--remote-allow-origins=*");
    options.setPageLoadStrategy(PageLoadStrategy.EAGER); // Быстрее загружает страницы
    
    try {
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        
        System.out.println("🌐 Navigating to: " + baseUrl);
        driver.get(baseUrl);
        
        // Проверка, что страница загрузилась
        String currentUrl = driver.getCurrentUrl();
        System.out.println("✅ Current URL: " + currentUrl);
        System.out.println("✅ Page title: " + driver.getTitle());
        
    } catch (Exception e) {
        System.err.println("❌ Failed to open browser: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}

    @BeforeEach
    void openSite(){
        // 🔥 Настраиваем Chrome для headless режима в CI
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);
    }

    @AfterEach
    void close() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @Test
    void testRunBrowser() throws InterruptedException {
        Thread.sleep(3000);
        Assertions.assertTrue(true); // простая проверка, что тест запустился
    }
       
    //Tests, bad practice
    @Test
    void testEmptyFields() throws InterruptedException {

        String expectedResult = "Поле обязательно для заполнения";
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[4]/button")).click();
        Thread.sleep(3000);
        String actualResult = driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[1]/span/span/span[3]")).getText().trim();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testHappyPathBadPractice() throws InterruptedException {

        String expectedResult = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[1]/span/span/span[2]/input")).sendKeys("Иванов Петр");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[2]/span/span/span[2]/input")).sendKeys("+79999999999");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[3]/label")).click();
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/form/div[4]/button")).click();
        Thread.sleep(3000);
        String actualResult = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/p")).getText().trim();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testHappyPathGoodPractice() throws InterruptedException {

        WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));

        String expectedResult = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Петр");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79999999999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id=order-success]")));
       // Thread.sleep(3000);
        String actualResult = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText().trim();
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void testLatinLetters() throws InterruptedException {
    }

    @Test
    void testNotClickedAgreementBox() throws InterruptedException {
    }

}
// //*[@id="welcome"]/p
// //*[@id="welcome"]/p
// //*[@id="welcome1"]/p[2]
