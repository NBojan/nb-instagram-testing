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

public class Comments {
    WebDriver driver;
    WebDriverWait wait;

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
    public String getComment(){
        return String.valueOf("c-" + Math.random() * 100);
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
        driver.navigate().refresh();
    }

    @Test(testName = "42", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidatePostingACommentOnAnyPost(){
        String comment = getComment();
        WebElement article = driver.findElement(By.xpath("//p[text()='JEFE']")).findElement(By.xpath("./ancestor::article"));
        WebElement input = article.findElement(By.cssSelector("input"));
        WebElement submitBtn = article.findElement(By.xpath(".//button[@type='submit']"));
        input.sendKeys(comment);
        submitBtn.click();
        WebElement newComment = article.findElement(By.xpath(".//p[text()='"+comment+"']"));
        Assert.assertTrue(newComment.isDisplayed(), "New comment is not displayed");
    }

    @Test(testName = "43", priority = 2, groups = {"validTc"}, enabled = true)
    public void ValidatePostingACommentOnAPersonalPost(){
        String comment = getComment();
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement input = article.findElement(By.cssSelector("input"));
        WebElement submitBtn = article.findElement(By.xpath(".//button[@type='submit']"));
        input.sendKeys(comment);
        submitBtn.click();
        WebElement newComment = article.findElement(By.xpath(".//p[text()='"+comment+"']"));
        Assert.assertTrue(newComment.isDisplayed(), "New comment is not displayed");
    }

    @Test(testName = "44", priority = 3, groups = {"validTc"}, enabled = true)
    public void VerifyCommentIncludesUsernameProfilePictureAndTime(){
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement commentsContainer = driver.findElement(By.xpath("//div[contains(@class, 'max-h-[6rem]')]"));
        Assert.assertTrue(commentsContainer.isDisplayed());
        WebElement profilePic = commentsContainer.findElement(By.cssSelector("img[alt='User']"));
        WebElement username = commentsContainer.findElement(By.cssSelector("p.truncate"));
        WebElement time = commentsContainer.findElement(By.cssSelector("p.whitespace-nowrap"));
        Assert.assertTrue(profilePic.isDisplayed(), "Profile pic is missing");
        Assert.assertTrue(username.isDisplayed(), "Username is missing");
        Assert.assertTrue(time.isDisplayed(), "Time is missing");
    }

    @Test(testName = "45", priority = 4, groups = {"validTc"}, enabled = false)
    public void ValidatePostCommentWontWorkIfOnlyWhiteSpacesInputted(){
        String comment = " ";
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement input = article.findElement(By.cssSelector("input"));
        WebElement submitBtn = article.findElement(By.xpath(".//button[@type='submit']"));
        input.sendKeys(comment);
        Assert.assertFalse(submitBtn.isEnabled(), "Button is enabled");
    }
}
