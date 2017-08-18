import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import jp.co.yahoo.mailsender.MailsenderApplication;
import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.service.MailInfo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import java.io.File;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration (classes = MailsenderApplication.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactListSteps {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Before
    public void setup() {
        if(driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.get("http://localhost:" + port + "/contact-list");
        }

        File file = new File(AddressBook.FILE_PATH);
        boolean isDelete = file.delete();
        System.out.println("file delete result is " + isDelete);
    }

    @After
    public void shutDown() {
        driver.quit();
    }

    @Given("^ContactList address is \"([^\"]*)\"$")
    public void address_is(String address) throws Throwable {
        driver.findElement(By.id("address")).sendKeys(address);
    }

    @Given("^ContactList is empty")
    public void clear_contact_list() throws Throwable {
        AddressBook addressBook = new AddressBook();
        addressBook.save();
    }

    @Given("^ContactList has \"([^\"]*)\"$")
    public void add_contact(String address) throws Throwable {
        AddressBook addressBook = new AddressBook();
        addressBook.add(new AddressItem(address));
        addressBook.save();
    }

    @When("^add$")
    public void add() throws Throwable {
        driver.findElement(By.id("add")).click();
    }

    @Then("^ContactList error_area is \"([^\"]*)\"$")
    public void error_area_is(String errorArea) throws Throwable {
        String actual = driver.findElement(By.id("error-area")).getText();

        Assert.assertEquals(errorArea, actual);
    }

    @Then("^ContactList address is added \"([^\"]*)\"$")
    public void contact_list_has(String address) throws Throwable {
        String list = driver.findElement(By.id("address-list")).getText();
        int index = list.indexOf(address);
        Assert.assertFalse("can not find address:" + address, index > 0);
    }
}
