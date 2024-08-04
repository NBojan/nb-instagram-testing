import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class LimitedAccess {
    WebDriver driver;

    String homeUrl = "https://nb-instagram.vercel.app/";
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
//    public void setup(){
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }

    @Test(testName = "1", groups = { "smoke" })
    public void UserNotLoggedInSuccessfullyOpensTheRegistrationLoginPage() {
        driver.get(registerUrl);
        String title = driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText();
        Assert.assertEquals("Sign up", title);
    }

    @Test(testName = "2", groups = { "smoke" })
    public void UserNotLoggedInTryingToAccessThePageRedirectedToTheLoginPage() {
        driver.get(homeUrl);
        String title = driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText();
        Assert.assertEquals("Sign up", title);
    }
}
