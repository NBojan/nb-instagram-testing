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

public class Login {
    WebDriver driver;

    String registerUrl = "https://nb-instagram.vercel.app/registration";

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
    }
//    @BeforeTest
//    public void setup() {
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//    }
    @AfterTest
    public void cleanUp() {
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(registerUrl);
    }

    @Test(testName = "15", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateLoginByEnteringValidEmailAndPassword(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());

        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
        userImg.click();
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();

        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "16", priority = 2, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateRedirectToDashboardAfterLogin(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());

        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        driver.findElement(By.name("password")).sendKeys("Testing123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();

        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
        userImg.click();
        Assert.assertEquals("https://nb-instagram.vercel.app/", driver.getCurrentUrl(), "url is: " + driver.getCurrentUrl());
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();

        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "17", priority = 3, groups = {"validTc"}, enabled = true)
    public void ValidateLoginButtonDisabledIfAtLeastOneFieldIsEmpty(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "18", priority = 4, groups = {"validTc"}, enabled = true)
    public void ValidateLoginButtonDisabledIfOnlySpacesAreEnteredInTheFields(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.name("email")).sendKeys(" ");
        driver.findElement(By.name("password")).sendKeys(" ");
        Assert.assertFalse(driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "19", priority = 5, groups = {"validTc"}, enabled = true)
    public void ValidateSwtichToRegisterLinkSwitchesToTheRegistrationForm(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.xpath("//span[text()='Sign Up']")).click();
        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "20", priority = 6, groups = {"validTc"}, enabled = true)
    public void ValidateLoginByUsingTheDummyAccount(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.xpath("//button[text()='use dummy account']")).click();

        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
        userImg.click();
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();

        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "21", priority = 7, groups = {"InvalidTc"}, enabled = true)
    public void ValidateErrorMessageIfValidEmailButWrongPassword(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.name("email")).sendKeys("mytesting@testing.com");
        driver.findElement(By.name("password")).sendKeys("randomwrongpassword");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid email or password.", errMsg.getText());
    }

    @Test(testName = "22", priority = 8, groups = {"InvalidTc"}, enabled = true)
    public void ValidateErrorMessageIfWrongEmail(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
        driver.findElement(By.name("email")).sendKeys("nonexistent@testing.com");
        driver.findElement(By.name("password")).sendKeys("randomwrongpassword");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid email or password.", errMsg.getText());
    }
}
