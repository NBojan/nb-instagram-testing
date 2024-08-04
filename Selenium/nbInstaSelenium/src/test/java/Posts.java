import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.time.Duration;

public class Posts {
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
    public String getCaption(){
        return String.valueOf(Math.random() * 100);
    }
    public void openSidebar(){
        WebElement uploadBtn = driver.findElement(By.xpath("//nav//button[contains(@class, 'transition-transform')]"));
        uploadBtn.click();
        WebElement sidebar = driver.findElement(By.cssSelector("#closeSidebar"));
        wait.until(ExpectedConditions.visibilityOf(sidebar));
        Assert.assertTrue(sidebar.isDisplayed());
    }
    public void closeSidebar(){
        driver.findElement(By.cssSelector("aside button.absolute")).click();
        Assert.assertFalse(driver.findElement(By.cssSelector("#closeSidebar")).isDisplayed());
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        login();
    }
//    @BeforeTest
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        login();
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void beforeEach(){
        openSidebar();
    }

    @Test(testName = "31", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateUploadingAPostByUsingAnImageAndACaption(){
        String caption = getCaption();
        File uploadFile = new File("src/test/images/postPic/1.jpg");
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        driver.findElement(By.cssSelector("input[name='caption']")).sendKeys(caption);
        driver.findElement(By.cssSelector("aside button[type='submit']")).click();
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("#closeSidebar"))));
        WebElement newPost = driver.findElement(By.xpath("//p[text()=" + caption + "]"));
        Assert.assertTrue(newPost.isDisplayed());
    }

    @Test(testName = "32", priority = 2, enabled =  true)
    public void ValidateRemovingAnImageAfterChoosing(){
        driver.navigate().refresh();
        openSidebar();
        File uploadFile = new File("src/test/images/postPic/1.jpg");
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        WebElement selectedFile = driver.findElement(By.cssSelector("img[alt='selectedFile']"));
        Assert.assertTrue(selectedFile.isDisplayed());
        selectedFile.click();
        Assert.assertEquals(driver.findElements(By.cssSelector("img[alt='selectedFile']")).size(), 0);
        closeSidebar();
    }

    @Test(testName = "33", priority = 3, enabled = true)
    public void ValidateUploadButtonIsDisabledIfNoImageSelected(){
        Assert.assertFalse(driver.findElement(By.cssSelector("aside button[type='submit']")).isEnabled());
        closeSidebar();
    }

    @Test(testName = "34", priority = 4, enabled = true)
    public void ValidateLinkToSwitchToProfilePictureUploadIsWorking(){
        driver.findElement(By.xpath("//span[contains(text(), 'Upload your profile picture')]")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//span[contains(text(), 'Upload a new post')]")).isDisplayed());
        closeSidebar();
    }

    @Test(testName = "35", priority = 5, enabled = true)
    public void ValidateCloseButtonClosesTheUploadBox(){
        driver.findElement(By.cssSelector("aside button.absolute")).click();
        Assert.assertFalse(driver.findElement(By.cssSelector("#closeSidebar")).isDisplayed());
    }

    @Test(testName = "36", priority = 6, enabled = false)
    public void ValidateUploadButtonIsDisabledIfOtherTypeOfFileIsSelected(){
        File uploadFile = new File("src/test/images/wrongformat/1.pdf");
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        Assert.assertFalse(driver.findElement(By.cssSelector("aside button[type='submit']")).isEnabled());
        closeSidebar();
    }
}
