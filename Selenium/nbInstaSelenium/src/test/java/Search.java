import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class Search {
    WebDriver driver;

    String registerUrl = "https://nb-instagram.vercel.app/registration";

    public void login(){
        driver.get(registerUrl);
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
    }
    public void dummyLogin(){
        driver.get(registerUrl);
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.xpath("//button[text()='use dummy account']")).click();
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
    }

//    @BeforeTest
//    @Parameters("browser")
//    public void setup(String browser) throws Exception {
//        if (browser.equalsIgnoreCase("firefox")) {
//            driver = new FirefoxDriver();
//        } else if (browser.equalsIgnoreCase("chrome")) {
//            driver = new ChromeDriver();
//        } else if (browser.equalsIgnoreCase("Edge")) {
//            driver = new EdgeDriver();
//        } else {
//            throw new Exception("Incorrect Browser");
//        }
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        login();
//    }
    @BeforeTest
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        login();
    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }

    @Test(testName = "30", priority = 1, groups = {"validTc"}, enabled = true)
    public void ValidateSearchingForAnExistingUsername(){
        String existingUsername = "dummies";
        WebElement search = driver.findElement(By.cssSelector("input#search"));
        search.sendKeys(existingUsername);
        search.sendKeys(Keys.ENTER);
        Assert.assertEquals(driver.getCurrentUrl(), "https://nb-instagram.vercel.app/search?user=" + existingUsername);
    }
}
