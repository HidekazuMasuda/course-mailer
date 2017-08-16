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

public class AttributeEditSteps {
    private WebDriver driver;

    @Before
    public void SetupWebDriver() {
        if(driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.get("http://localhost:8888/attribute/edit");
        }
    }

    @After
    public void shutDownWebDriver() {
        driver.quit();
    }

    @Given("^first_name is \"([^\"]*)\"$")
    public void first_name_is(String first_name) throws Throwable {
        driver.findElement(By.id("first_name")).sendKeys(first_name);
    }

    @Given("^last_name is \"([^\"]*)\"$")
    public void last_name_is(String last_name) throws Throwable {
        driver.findElement(By.id("last_name")).sendKeys(last_name);
    }

    @Given("^attr1_field is \"([^\"]*)\"$")
    public void attr1_field_is(String attr1_field) throws Throwable {
        driver.findElement(By.id("attr1_field")).sendKeys(attr1_field);
    }

    @Given("^attr1_value is \"([^\"]*)\"$")
    public void attr1_value_is(String attr1_value) throws Throwable {
        driver.findElement(By.id("attr1_value")).sendKeys(attr1_value);
    }

    @Given("^attr2_field is \"([^\"]*)\"$")
    public void attr2_field_is(String attr2_field) throws Throwable {
        driver.findElement(By.id("attr2_field")).sendKeys(attr2_field);
    }

    @Given("^attr2_value is \"([^\"]*)\"$")
    public void attr2_value_is(String attr2_value) throws Throwable {
        driver.findElement(By.id("attr2_value")).sendKeys(attr2_value);
    }

    @When("^save$")
    public void save() throws Throwable {
        driver.findElement(By.id("save")).click();
    }

    @Then("^attribute edit page error_area is \"([^\"]*)\"$")
    public void error_area_is(String errorArea) throws Throwable {
        String actual = driver.findElement(By.id("error-area")).getText();
        Assert.assertEquals(errorArea, actual);
    }
}
