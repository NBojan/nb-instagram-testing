import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class Username {
    WebDriver driver;
    WebDriverWait wait;

    String registerUrl = "https://nb-instagram.vercel.app/registration";
    String profileUrl = "https://nb-instagram.vercel.app/profile/";

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
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        login();
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @AfterMethod
    public void refresh(){
        driver.navigate().back();
    }

    @Test(testName = "46", priority = 1, groups = {"validTc"}, enabled = true)
    public void ValidateUsernameInPostHeaderIsALinkToTheUserPage(){
        WebElement username = driver.findElements(By.cssSelector("article")).getFirst().findElements(By.cssSelector("h5")).getFirst();
        Assert.assertTrue(username.isDisplayed());
        username.click();
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(profileUrl));
    }

    @Test(testName = "47", priority = 2, groups = {"validTc"}, enabled = true)
    public void ValidateUsernameInPostCaptionIsALinkToTheUserPage(){
        WebElement username = driver.findElements(By.cssSelector("article")).getFirst().findElement(By.cssSelector("h5.flex-shrink-0"));
        Assert.assertTrue(username.isDisplayed());
        username.click();
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(profileUrl));
    }

    @Test(testName = "48", priority = 3, groups = {"validTc"}, enabled = true)
    public void ValidateUsernameInPostCommentIsALinkToTheUserPage(){
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement username = article.findElement(By.cssSelector("p.truncate"));
        Assert.assertTrue(username.isDisplayed());
        username.click();
        String url = driver.getCurrentUrl();
        Assert.assertTrue(url.contains(profileUrl));
    }
}
