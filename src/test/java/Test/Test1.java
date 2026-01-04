package Test;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;

public class Test1 {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @Test
    public void amazonProductWorkflow() {

        try {

            driver.get("https://www.amazon.com");


            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("twotabsearchtextbox"))).sendKeys("Laptop");

            driver.findElement(By.id("nav-search-submit-button")).click();


            WebElement firstProduct = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("(//h2[@class='a-size-medium a-spacing-none a-color-base a-text-normal'])[1]")
                    )
            );


            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstProduct);
            js.executeScript("arguments[0].click();", firstProduct);


            WebElement productPageLoaded = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//h1[@id='title'] | //input[@id='add-to-cart-button'] | //span[text()='See All Buying Options']")
                    )
            );

            Assert.assertTrue(productPageLoaded.isDisplayed(),
                    "Product details page did not load");


            takeScreenshot("ProductPage.png");

        } catch (Exception e) {
            // Take screenshot even if test fails
            takeScreenshot("FailureScreenshot.png");
            throw e;
        }
    }


    public void takeScreenshot(String fileName) {
        try {
            File screenshotsDir = new File("screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdir();
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileHandler.copy(src, new File("screenshots/" + fileName));

            System.out.println("Screenshot saved: screenshots/" + fileName);
        } catch (Exception ex) {
            System.out.println("Screenshot failed: " + ex.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}
