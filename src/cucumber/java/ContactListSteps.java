import com.odde.mailsender.MailsenderApplication;
import com.odde.mailsender.data.AddressBook;
import com.odde.mailsender.data.AddressItem;
import com.odde.mailsender.service.AddressBookService;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.File;

import static org.hamcrest.core.Is.is;

@ContextConfiguration(classes = MailsenderApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ContactListSteps {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (driver != null)
                    driver.quit();
            }
        });
    }

    @Autowired
    AddressBookService addressBookService;

    @Before
    public void setup() throws Exception {
        setupDriver();

        setupAddressItem();
    }

    private void setupDriver() {
        if (driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
        }
        driver.get("http://localhost:" + port + "/contact-list");
    }

    private void setupAddressItem() throws Exception {
        File file = new File(AddressBook.FILE_PATH);
        boolean isDelete = file.delete();

        addressBookService.add(new AddressItem("user1@gmail.com", "user1"));
        addressBookService.add(new AddressItem("user2@gmail.com", "user2"));
        addressBookService.add(new AddressItem("noname@gmail.com", ""));
    }

    @After
    public void tearDown() {
        File file = new File(AddressBook.FILE_PATH);
        boolean isDelete = file.delete();
        System.out.println("file delete result is " + isDelete);
    }

    @Given("^ContactList address is \"([^\"]*)\"$")
    public void address_is(String address) throws Throwable {
        driver.findElement(By.id("address")).sendKeys(address);
    }

    @Given("^ContactList name is \"([^\"]*)\"$")
    public void name_is(String name) throws Throwable {
        driver.findElement(By.id("name")).sendKeys(name);
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
    public void contact_list_address_has(String address) throws Throwable {
        String html = driver.findElement(By.id("address-list")).getText();
        Assert.assertThat(html.contains(address), is(true));
    }

    @Then("^ContactList name is added \"([^\"]*)\"$")
    public void contact_list_name_has(String name) throws Throwable {
        String html = driver.findElement(By.id("address-list")).getText();
        Assert.assertThat(html.contains(name), is(true));
    }

    @When("^create email$")
    public void create_email() throws Throwable {
        driver.findElement(By.id("create-email")).click();
    }

    @Then("^MailSender address is \"([^\"]*)\"$")
    public void mail_sender_address_is(String address) throws Throwable {
        String html = driver.findElement(By.id("address")).getAttribute("value");
        Assert.assertEquals(address, html);
    }

    @Given("^No ContactList is checked$")
    public void no_contact_list_is_checked() throws Throwable {
    }

    @Given("^checked ContactList is \"([^\"]*)\"$")
    public void checked_contact_list_is(String address) throws Throwable {
        driver.findElement(By.cssSelector("input[value='" + address + "']")).click();
    }

    @Given("^checked all ContactList$")
    public void checked_all_contact_list() throws Throwable {
        driver.findElement(By.id("all")).click();
    }
}
