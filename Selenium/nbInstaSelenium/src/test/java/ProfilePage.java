import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class ProfilePage {
    WebDriver driver;
    WebDriverWait wait;

    String homeUrl = "https://nb-instagram.vercel.app/";
    String registerUrl = "https://nb-instagram.vercel.app/registration";
    String profileUrl = "https://nb-instagram.vercel.app/profile/MyTestingAccount";

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
        driver.get(profileUrl);
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        login();
//        driver.get(profileUrl);
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void getProfilePage(){
        driver.get(profileUrl);
    }

    @Test(testName = "49", priority = 1, groups = {"validTc"}, enabled = true)
    public void VerifyProfilePictureUsernameNameAndNumberOfPostsAppear(){
        WebElement profilePic = driver.findElement(By.cssSelector("main")).findElement(By.cssSelector("img[alt='userImg']"));
        WebElement username = driver.findElement(By.cssSelector("main")).findElement(By.xpath("//p[text()='MyTestingAccount']"));
        WebElement name = driver.findElement(By.cssSelector("main")).findElement(By.xpath("//p[contains(@class, 'text-gray-700')]"));
        WebElement numberOfPosts = driver.findElement(By.cssSelector("main")).findElement(By.xpath("//p[text()='posts']"));

        Assert.assertTrue(profilePic.isDisplayed());
        Assert.assertTrue(username.isDisplayed());
        Assert.assertTrue(name.isDisplayed());
        Assert.assertTrue(numberOfPosts.isDisplayed());
    }

    @Test(testName = "50", priority = 2, groups = {"smoke", "validTc"}, enabled = true)
    public void VerifyUsersPostsAreDisplayed(){
        WebElement postsContainer = driver.findElement(By.cssSelector("div.flex-wrap"));
        Assert.assertTrue(postsContainer.isDisplayed());
        WebElement post = postsContainer.findElements(By.cssSelector("a.block")).getFirst();
        Assert.assertTrue(post.isDisplayed());
    }

    @Test(testName = "51", priority = 3, groups = {"smoke", "validTc"}, enabled = true)
    public void VerifyMessageThereAreNoPostsToShowForAUserWithNoPosts(){
        String message = "There are no posts to show...";
        String profile = "https://nb-instagram.vercel.app/profile/helloWorld";
        driver.get(profile);
        WebElement displayedMessage = driver.findElement(By.xpath("//h4[text()='" + message + "']"));
        Assert.assertTrue(displayedMessage.isDisplayed());
    }

    @Test(testName = "52", priority = 4, groups = {"validTc"}, enabled = true)
    public void VerifyNumberOfLikesAndCommentsAppearWhileHoveringOnAPost() {
        Actions actions = new Actions(driver);
        WebElement post = driver.findElements(By.cssSelector("a.block")).getFirst();
        actions.moveToElement(post).perform();
        WebElement detailsContainer = post.findElement(By.cssSelector("div.bg-black"));
        Assert.assertTrue(detailsContainer.isDisplayed());
    }

    @Test(testName = "53", priority = 5, groups = {"validTc"}, enabled = true)
    public void ValidatePostsAreLinksTowardsThePostPage() throws InterruptedException {
        WebElement post = driver.findElements(By.cssSelector("a.block")).getFirst();
        post.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("https://nb-instagram.vercel.app/post/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("https://nb-instagram.vercel.app/post/"));
    }
}
