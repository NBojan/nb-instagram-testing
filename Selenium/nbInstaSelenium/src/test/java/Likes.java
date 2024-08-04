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

public class Likes {
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

    @Test(testName = "37", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateLikingAPostFromAnotherUser(){
        WebElement article = driver.findElement(By.xpath("//p[text()='JEFE']")).findElement(By.xpath("./ancestor::article"));
        WebElement likeBtn = article.findElements(By.cssSelector(".interact-btn")).getFirst();
        likeBtn.click();
        String classes = likeBtn.getAttribute("class");
        Assert.assertTrue(classes.contains("text-red-500"), "Button did not turn red");
        // remove like
        likeBtn.click();
        String classesNew = likeBtn.getAttribute("class");
        Assert.assertFalse(classesNew.contains("text-red-500"), "Button did not get like removed");
    }

    @Test(testName = "38", priority = 2, groups = {"validTc"}, enabled = true)
    public void ValidateLikingAPersonalPost() {
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement likeBtn = article.findElements(By.cssSelector(".interact-btn")).getFirst();
        likeBtn.click();
        String classes = likeBtn.getAttribute("class");
        Assert.assertTrue(classes.contains("text-red-500"), "Button did not turn red");
        // remove like
        likeBtn.click();
        String classesNew = likeBtn.getAttribute("class");
        Assert.assertFalse(classesNew.contains("text-red-500"), "Button did not get like removed");
    }

    @Test(testName = "39", priority = 3, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateRemovingALikeFromAPost() {
        WebElement article = driver.findElement(By.xpath("//p[text()='seleniumRemoveLike']")).findElement(By.xpath("./ancestor::article"));
        WebElement likeBtn = article.findElement(By.cssSelector(".text-red-500"));
        likeBtn.click();
        String classes = likeBtn.getAttribute("class");
        Assert.assertFalse(classes.contains("text-red-500"), "Button did not get like removed");
        // add  like
        likeBtn.click();
        String classesNew = likeBtn.getAttribute("class");
        Assert.assertTrue(classesNew.contains("text-red-500"), "Button did not turn red");
    }

    @Test(testName = "40", priority = 4, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateNumberOfLikesIsDisplayedForAPostThatHas1OrMoreLikes() {
        WebElement article = driver.findElement(By.xpath("//p[text()='My car is better than yours.']")).findElement(By.xpath("./ancestor::article"));
        Assert.assertTrue(article.isDisplayed());
        WebElement likes = article.findElement(By.xpath(".//h5[text()='likes']"));
        Assert.assertTrue(likes.isDisplayed());
    }

    @Test(testName = "41", priority = 5, groups = {"validTc"}, enabled = true)
    public void ValidateClickingOnTheLikesOpensABoxShowingTheUsersThatLiked() {
        WebElement article = driver.findElement(By.xpath("//p[text()='My car is better than yours.']")).findElement(By.xpath("./ancestor::article"));
        Assert.assertTrue(article.isDisplayed());
        WebElement likes = article.findElement(By.xpath(".//h5[text()='likes']"));
        Assert.assertTrue(likes.isDisplayed());
        likes.click();
        WebElement likesBox = driver.findElement(By.cssSelector("#closeLikes"));
        Assert.assertTrue(likesBox.isDisplayed());
        int usersWhoLiked = likesBox.findElements(By.cssSelector("img")).size();
        Assert.assertTrue(usersWhoLiked > 0, "No users displayed");
    }
}
