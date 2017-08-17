import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import jp.co.yahoo.mailsender.MailsenderApplication;
import jp.co.yahoo.mailsender.service.MailInfo;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration (classes = MailsenderApplication.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MailSendSteps {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private Wiser wiser;
    private List<WiserMessage> messages;

    @Before
    public void setup() {
        if(driver == null) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver();
            driver.get("http://localhost:" + port + "/send");
        }
        wiser = new Wiser();
        wiser.setPort(2500);
        wiser.setHostname("localhost");
        wiser.start();

    }

    @After
    public void shutDown() {
        driver.quit();
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

    @Then("^should receive the following emails:$")
    public void should_receive_the_following_emails(List<MailInfo> table) throws Throwable {

        assertThat(table.size(), is(wiser.getMessages().size()));

        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)

    }

    @Then("^receive mail count is (\\d+)$")
    public void receive_mail_count_is(String count) throws Throwable {
        messages = wiser.getMessages();
        assertThat(messages.size(), is(new Integer(count)));
    }

    @Then("^receive mail from is \"([^\"]*)\"$")
    public void receive_mail_from_is(String from) throws Throwable {
        WiserMessage wiserMessage = messages.get(0);
        assertThat(wiserMessage.getEnvelopeSender(), is(from));
    }

    @Then("^receive mail to is \"([^\"]*)\"$")
    public void receive_mail_to_is(String to) throws Throwable {
        WiserMessage wiserMessage = messages.get(0);
        assertThat(wiserMessage.getEnvelopeReceiver(), is(to));
    }

    @Then("^receive mail subject is \"([^\"]*)\"$")
    public void receive_mail_subject_is(String subject) throws Throwable {
        WiserMessage wiserMessage = messages.get(0);
        assertThat(wiserMessage.getMimeMessage().getSubject(), is(subject));
    }

    @Then("^receive mail body is \"([^\"]*)\"$")
    public void receive_mail_body_is(String body) throws Throwable {
        WiserMessage wiserMessage = messages.get(0);
        assertThat(new String(wiserMessage.getData()).contains(body), is(true));
    }
}
