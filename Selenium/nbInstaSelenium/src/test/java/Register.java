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
import java.util.Random;

public class Register {
    WebDriver driver;

    String registerUrl = "https://nb-instagram.vercel.app/registration";
    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }
    protected String getSaltStringCyrilic() {
        String SALTCHARS = "АБВГДЃЕЖЗSИЈКЛЉМНЊОПРСТЌУФХЦЧЏШ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
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
    }
//    @BeforeTest
//    public void setup() {
//        WebDriverManager.firefoxdriver().setup();
//        driver = new FirefoxDriver();
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//    }
    @AfterTest
    public void cleanUp(){
        driver.quit();
    }
    @BeforeMethod
    public void refresh(){
        driver.get(registerUrl);
    }

    @Test(testName = "3", priority = 1, groups = {"smoke", "validTc"}, enabled = true)
    public void ValidateRegistrationWithValidDataInAllFields(){
        String randomEmail = getSaltString()+"@gmail.com";
        String randomUsername = getSaltString();
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys(randomUsername);
        driver.findElement(By.name("email")).sendKeys(randomEmail);
        driver.findElement(By.name("password")).sendKeys("Testot123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
        userImg.click();
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();
        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "4", priority = 2, groups = {"validTc"})
    public void ValidateEmailPlaceholderIsEmailAddressCom(){
        String emailPlaceholder = driver.findElement(By.name("email")).getAttribute("placeholder");
        Assert.assertEquals("email@address.com", emailPlaceholder);
    }

    @Test(testName = "5", priority = 3, groups = {"validTc"})
    public void ValidateSubmitButtonDisabledIfAtLeastOneFieldIsEmpty(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("testot3");
        driver.findElement(By.name("email")).sendKeys("testot3@gmail.com");
        Assert.assertEquals(false, driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "6", priority = 4, groups = {"validTc"})
    public void ValidateSubmitButtonDisabledIfOnlySpacesAreEnteredInTheFields(){
        driver.findElement(By.name("firstName")).sendKeys(" ");
        driver.findElement(By.name("lastName")).sendKeys(" ");
        driver.findElement(By.name("username")).sendKeys(" ");
        driver.findElement(By.name("email")).sendKeys(" ");
        driver.findElement(By.name("password")).sendKeys("  ");
        Assert.assertEquals(false, driver.findElement(By.xpath("//button[text()='Submit']")).isEnabled());
    }

    @Test(testName = "7", priority = 5, groups = {"validTc", "smoke"}, enabled = false)
    public void ValidateRegistrationWithCyrilicLettersUsedInFirstLastNameAndUsername(){
        String randomEmail = getSaltString()+"@gmail.com";
        String randomUsername = getSaltStringCyrilic();
        driver.findElement(By.name("firstName")).sendKeys("Боко");
        driver.findElement(By.name("lastName")).sendKeys("Новески");
        driver.findElement(By.name("username")).sendKeys(randomUsername);
        driver.findElement(By.name("email")).sendKeys(randomEmail);
        driver.findElement(By.name("password")).sendKeys("Testot123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement userImg = driver.findElement(By.xpath("//img[@alt='userImg']"));
        Assert.assertTrue(userImg.isDisplayed());
        userImg.click();
        driver.findElement(By.xpath("//button[text()='Log out.']")).click();
        Assert.assertEquals("Sign up", driver.findElement(By.xpath("//h3[contains(text(), 'Sign up')]")).getText());
    }

    @Test(testName = "8", priority = 6, groups = {"validTc", "smoke"})
    public void ValidateSwitchToLoginLinkSwitchesToTheLoginForm(){
        driver.findElement(By.xpath("//span[text()='Log In']")).click();
        Assert.assertEquals("Log in", driver.findElement(By.xpath("//h3[contains(text(), 'Log in')]")).getText());
    }

    @Test(testName = "9", priority = 7, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringUsernameAlreadyInUse(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("dummies");
        driver.findElement(By.name("email")).sendKeys("hellohello@hellohello.com");
        driver.findElement(By.name("password")).sendKeys("Testot123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Username is already in use.", errMsg.getText());
    }

    @Test(testName = "10", priority = 8, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringEmailAlreadyInUse(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("Newusername123");
        driver.findElement(By.name("email")).sendKeys("digmark27@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Testot123");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Email is already in use.", errMsg.getText());
    }

    @Test(testName = "11", priority = 9, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringPasswordLessThan8Chars(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("Newusername123");
        driver.findElement(By.name("email")).sendKeys("testot5@gmail.com");
        driver.findElement(By.name("password")).sendKeys("Bojan12");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.", errMsg.getText());
    }

    @Test(testName = "12", priority = 10, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringPasswordWithoutLowercaseLetters(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("Newusername123");
        driver.findElement(By.name("email")).sendKeys("testot5@gmail.com");
        driver.findElement(By.name("password")).sendKeys("BOJAN12345");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.", errMsg.getText());
    }

    @Test(testName = "13", priority = 11, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringPasswordWithoutUppercaseLetters(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("Newusername123");
        driver.findElement(By.name("email")).sendKeys("testot5@gmail.com");
        driver.findElement(By.name("password")).sendKeys("bojan12345");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.", errMsg.getText());
    }

    @Test(testName = "14", priority = 11, groups = {"InvalidTc"})
    public void ValidateErrorMessageDisplaysIfEnteringPasswordWithoutNumbers(){
        driver.findElement(By.name("firstName")).sendKeys("Test");
        driver.findElement(By.name("lastName")).sendKeys("TestovskiS");
        driver.findElement(By.name("username")).sendKeys("Newusername123");
        driver.findElement(By.name("email")).sendKeys("testot5@gmail.com");
        driver.findElement(By.name("password")).sendKeys("bojanNov");
        driver.findElement(By.xpath("//button[text()='Submit']")).click();
        WebElement errMsg = driver.findElement(By.xpath("//p[contains(@class, 'text-red-500')]"));
        Assert.assertTrue(errMsg.isDisplayed());
        Assert.assertEquals("Invalid password. Min. 8 charachted, at least 1 lowercase, 1 uppercase and 1 number.", errMsg.getText());
    }
}

//testot3 testot3@gmail.com Test TestovskiS Testot123
//Боко Новески бокобокица testot4@gmail.com Testot123
// WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
// wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'COMPOSE')]")));
//