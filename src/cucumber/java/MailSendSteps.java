import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import jp.co.yahoo.mailsender.MailsenderApplication;
import jp.co.yahoo.mailsender.data.AddressBook;
import jp.co.yahoo.mailsender.data.AddressItem;
import jp.co.yahoo.mailsender.service.AddressBookService;
import jp.co.yahoo.mailsender.service.MailInfo;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@ContextConfiguration(classes = MailsenderApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailSendSteps {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private Wiser wiser;

    @Autowired
    AddressBookService addressBookService;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if(driver != null)
                    driver.quit();
            }
        });
    }

    @Before
    public void setup() throws Exception {
        if (driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
        }

        driver.get("http://localhost:" + port + "/send");
        wiser = new Wiser();
        wiser.setPort(2500);
        wiser.setHostname("localhost");
        wiser.start();

        File file = new File(AddressBook.FILE_PATH);
        boolean isDelete = file.delete();

        addressBookService.add(new AddressItem("user1@gmail.com", "user1"));
        addressBookService.add(new AddressItem("user2@gmail.com", "user2"));
        addressBookService.add(new AddressItem("noname@gmail.com", ""));
    }

    @After
    public void shutDown() {
        wiser.stop();
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

    @Then("^error_area is none$")
    public void error_area_is_none() throws Throwable {
        try {
            driver.findElement(By.id("error-area"));
            fail();
        } catch (NoSuchElementException e) {
            /* こちらが正常系*/
            return;
        }
    }

    @Then("^should receive the following emails:$")
    public void should_receive_the_following_emails(List<MailInfo> mails) throws Throwable {
        List<WiserMessage> messages = wiser.getMessages();
        for (int i = 0; i < mails.size(); i++) {
            WiserMessage wiserMessage = messages.get(i);
            assertThat(wiserMessage.getEnvelopeSender(), is(mails.get(i).getFrom()));
            assertThat(wiserMessage.getEnvelopeReceiver(), is(mails.get(i).getTo()));
            assertThat(wiserMessage.getMimeMessage().getSubject(), is(mails.get(i).getSubject()));
            assertThat(wiserMessage.toString(), containsString(mails.get(i).getBody()));
        }
    }
}
