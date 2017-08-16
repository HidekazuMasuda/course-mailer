import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MailSendSteps {

    private WebDriver driver;

    @Before
    public void SetupWebDriver() {
        if(driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.get("http://localhost:8888/send");
        }
    }

    @After
    public void shutDownWebDriver() {
        driver.quit();
    }

    @Given("^address is \"([^\"]*)\"$")
    public void address_is(String address) throws Throwable {
        driver.findElement(By.id("address")).sendKeys(address);
    }

    @Given("^subject is \"([^\"]*)\"$")
    public void subject_is(String subject) throws Throwable {
        driver.findElement(By.id("subject")).sendKeys(subject);
    }

    @Given("^body is \"([^\"]*)\"$")
    public void body_is(String body) throws Throwable {
        driver.findElement(By.id("body")).sendKeys(body);
    }

    @When("^send$")
    public void send() throws Throwable {
        driver.findElement(By.id("send")).click();
    }

    @Then("^error_area is \"([^\"]*)\"$")
    public void error_area_is(String errorArea) throws Throwable {
        String actual = driver.findElement(By.id("error-area")).getText();
        Assert.assertEquals(errorArea, actual);
    }
}
