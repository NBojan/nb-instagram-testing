import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class Header {
    WebDriver driver;

    String homeUrl = "https://nb-instagram.vercel.app/";
    String dummyUrl = "https://nb-instagram.vercel.app/profile/dummies";
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

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) throws Exception {
        if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        } else {
            throw new Exception("Incorrect Browser");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        login();
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.firefoxdriver().setup();
//        driver = new FirefoxDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        login();
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @AfterMethod
    public void refresh(){
        driver.navigate().refresh();
    }

    @Test(testName = "23", priority = 1, groups = {"validTc"}, enabled = true)
    public void VerifyInstagramLogoVisible(){
        WebElement logo = driver.findElement(By.xpath("//img[@alt='Instagram']"));
        Assert.assertTrue(logo.isDisplayed());
    }

    @Test(testName = "24", priority = 2, groups = {"validTc"}, enabled = true)
    public void VerifyInstagramLogoIsALinkToTheDashboard(){
        driver.get(dummyUrl);
        Assert.assertEquals(dummyUrl, driver.getCurrentUrl(), "Page did not go to the dummy url");
        driver.findElement(By.xpath("//img[@alt='Instagram']")).click();
        Assert.assertEquals(homeUrl, driver.getCurrentUrl(), "Page did not go to the homepage");
    }

    @Test(testName = "25", priority = 3, groups = {"validTc"}, enabled = true)
    public void VerifyTheUploadPostButtonIsVisible(){
        WebElement uploadBtn = driver.findElement(By.xpath("//nav//button[contains(@class, 'transition-transform')]"));
        Assert.assertTrue(uploadBtn.isDisplayed());
    }

    @Test(testName = "26", priority = 4, groups = {"validTc"}, enabled = true)
    public void VerifyTheProfilePictureIsVisible(){
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
    }

    @Test(testName = "27", priority = 5, groups = {"validTc"}, enabled = true)
    public void VerifyTheProfilePictureIsClickableAndTogglesTheProfileMenu(){
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        userImg.click();
        Assert.assertTrue(driver.findElement(By.xpath("//nav//div[contains(@class, 'absolute')]")).isDisplayed());
        userImg.click();
        Assert.assertEquals(0, driver.findElements(By.xpath("//nav//div[contains(@class, 'absolute')]")).size());
    }
}
