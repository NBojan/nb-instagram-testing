import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

public class ProfileMenu {
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

    @Test(testName = "28", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateLogOutByClickingOnTheLogOutOption(){
        driver.findElement(By.xpath("//img[@alt='userImg']")).click();
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();
        Assert.assertTrue(driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).isDisplayed());
    }

    @Test(testName = "29", priority = 2, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateUpdatingTheProfilePicture() throws InterruptedException {
        login();
        File uploadFile = new File("src/test/images/pp/1.jpg");
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        String existingPic = userImg.getAttribute("src");
        userImg.click();
        driver.findElement(By.xpath("//button[text()='Update profile picture.']")).click();
        driver.findElement(By.cssSelector("input[type='file']")).sendKeys(uploadFile.getAbsolutePath());
        driver.findElement(By.cssSelector("aside button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.cssSelector("#closeSidebar"))));

        Assert.assertFalse(driver.findElement(By.cssSelector("#closeSidebar")).isDisplayed());
        driver.navigate().refresh();
        String newPic = driver.findElement(By.xpath("//img[@alt='userImg']")).getAttribute("src");
        Assert.assertNotEquals(existingPic, newPic);
    }
}
