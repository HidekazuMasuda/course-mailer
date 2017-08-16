package jp.co.yahoo.mailsender.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ContactListControllerTests {
	ContactListController controller = new ContactListController();

	@Test
	public void showContactList() {
		assertThat(controller.contactList(null), is("contact-list"));
	}

	@Test
	public void addContactList() {
		assertThat(controller.contactList(null), is("contact-list"));
	}

}
