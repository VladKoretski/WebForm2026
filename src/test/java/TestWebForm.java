import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestWebForm {

    //Запустить браузер
    //Открыть сайт
    //Заполнить форму и нажать кнопку
    //Проверить наличие ожидаемого результата
    //Все закрыть
    //посмотрть отчет
    //Отправить в репозиторий и сделать CI

    //Подготовка среды
    WebDriver driver;

    @BeforeAll
    static void prepareBrowser(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void openSite(){
        driver = new ChromeDriver();
        driver.get("http://localhost:7777/");
    }

    @AfterEach
    void close() {
        driver.quit();
        driver=null;
    }

    @Test
    void testRunBrowser () throws InterruptedException {
        Thread.sleep(3000);
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