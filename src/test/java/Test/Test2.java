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

public class Test2 {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void automateAmazonWorkflow() throws Exception {

        navigateToHomePage();
        searchProduct("laptop");
        clickFirstSearchResult();
        validateProductDetailsPage();
        takeScreenshotAfterValidation("ProductDetailsPage.png");

        Assert.assertTrue(true, "Amazon product workflow executed successfully");
    }

    public void navigateToHomePage() {
        driver.get("https://www.amazon.com");
    }

    public void searchProduct(String productName) {
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox"))
        );
        searchBox.sendKeys(productName);
        driver.findElement(By.id("nav-search-submit-button")).click();
    }

    public void clickFirstSearchResult() {
        WebElement firstProduct = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("(//h2[@class='a-size-medium a-spacing-none a-color-base a-text-normal'])[1]")
                )
        );

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstProduct);
        js.executeScript("arguments[0].click();", firstProduct);
    }

    public void validateProductDetailsPage() {

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.id("productTitle")),
                ExpectedConditions.presenceOfElementLocated(By.id("centerCol")),
                ExpectedConditions.presenceOfElementLocated(By.id("dp-container"))
        ));
    }

    public void takeScreenshotAfterValidation(String fileName) throws Exception {
        File screenshotDir = new File("screenshots");
        if (!screenshotDir.exists()) {
            screenshotDir.mkdir();
        }

        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileHandler.copy(src, new File("screenshots/" + fileName));

        System.out.println("Screenshot saved at: screenshots/" + fileName);
    }

    @AfterTest
    public void tearDown() {
        driver.quit();
    }
}

