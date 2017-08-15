import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class CensorSteps {

    private WebDriver driver;

    @Before
    public void SetupWebDriver() {
        if(driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.get("http://localhost:8888/censor-sample");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> driver.quit()));
    }

    @Given("^headline is \"([^\"]*)\"$")
    public void headline_is(String headline) throws Throwable {
        driver.findElement(By.id("headline")).sendKeys(headline);
    }

    @Given("^censored word is \"([^\"]*)\"$")
    public void censored_word_is(String word) throws Throwable {
        driver.findElement(By.id("censor-word")).sendKeys(word);
    }

    @When("^censor$")
    public void censor() throws Throwable {
        driver.findElement(By.id("submit")).click();
    }

    @Then("^censored headline is \"([^\"]*)\"$")
    public void censored_headline_is(String censoredHeadline) throws Throwable {
        String actual = driver.findElement(By.id("censored-result")).getText();
        Assert.assertEquals(censoredHeadline, actual);
    }
}
