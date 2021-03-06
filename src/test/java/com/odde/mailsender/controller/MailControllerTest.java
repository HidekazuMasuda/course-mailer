package com.odde.mailsender.controller;

import com.odde.mailsender.data.ContactListImpl;
import com.odde.mailsender.service.ContactListService;
import com.odde.mailsender.service.MailInfo;
import com.odde.mailsender.service.MailService;
import com.odde.mailsender.data.Contact;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

import static com.odde.mailsender.service.MailBuilder.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MailControllerTest {

    public static final String MAY_NOT_BE_EMPTY = "{0} may not be empty";
    public static final String ADDRESS_FORMAT_IS_WRONG = "Address format is wrong";
    public static final String WHEN_YOU_USE_TEMPLATE_CHOOSE_EMAIL_FROM_CONTRACT_LIST_THAT_HAS_A_NAME = "When you use template, choose email from contract list that has a name";
    public static final String TRY_TO_SEND_EMAIL_BUT_FAILED = "Try to send email, but failed";

    @MockBean
    private MailService mailService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ContactListService contactListService;

    Contact noNameAddress = new Contact("course.mailer@gmail.com", "");

    @Before
    public void setUp() {
        File file = new File(ContactListImpl.FILE_PATH);
        boolean isDelete = file.delete();
    }

    @Test
    public void sendToMultipleAddresses() throws Exception {
        getPerform(validMail().withTo("abc@gmail.com;john@gmail.com").build())
                .andExpect(view().name("redirect:/send"));

        verify(mailService, times(1)).sendMultiple(any());
    }

    @Test
    public void showErrorIfEmptyForms() throws Exception {
        MvcResult mvcResult = getPerform(validMail().withTo("").withSubject("").withBody("").build())
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "address", MAY_NOT_BE_EMPTY);
        assertErrorMessage(mvcResult, "subject", MAY_NOT_BE_EMPTY);
        assertErrorMessage(mvcResult, "body", MAY_NOT_BE_EMPTY);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void manyAddressWithInvalidAddressAndNoSubject() throws Exception {
        MvcResult mvcResult = getPerform(validMail().withTo("abcdefghi123@xxx.com ; xxx.com; stanly@xxx.com").withSubject("").build())
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "address", ADDRESS_FORMAT_IS_WRONG);
        assertErrorMessage(mvcResult, "subject", MAY_NOT_BE_EMPTY);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void sendMultipleWhenUseTemplate() throws Exception {

        contactListService.add(new Contact("course.mailer@gmail.com", "Aki"));
        contactListService.add(new Contact("stanly@xxx.com", "Stanly"));

        MailInfo mailInfo = validMail().withSubject("Hello $name").withBody("Hi $name").withTo("course.mailer@gmail.com;stanly@xxx.com").build();

        getPerform(mailInfo)
                .andExpect(view().name("redirect:/send"));

        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getSubject().equals("Hello Aki")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(1).getSubject().equals("Hello Stanly")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(0).getBody().equals("Hi Aki")));
        verify(mailService).sendMultiple(argThat(mailInfoList -> mailInfoList.get(1).getBody().equals("Hi Stanly")));
    }

    @Test
    public void notSubjectReplaceWhenNotRegisteredAddress() throws Exception {
        MvcResult mvcResult = getPerform(validMail().withSubject("Hello $name").withTo("foobar@xxx.com").build())
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "", WHEN_YOU_USE_TEMPLATE_CHOOSE_EMAIL_FROM_CONTRACT_LIST_THAT_HAS_A_NAME);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notBodyReplaceWhenNotRegisteredAddress() throws Exception {
        MvcResult mvcResult = getPerform(validMail().withBody("Hi $name").withTo("foobar@xxx.com").build())
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "", WHEN_YOU_USE_TEMPLATE_CHOOSE_EMAIL_FROM_CONTRACT_LIST_THAT_HAS_A_NAME);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notSubjectReplaceWhenNoNameAddress() throws Exception {
        contactListService.add(noNameAddress);

        MailInfo mailInfo = validMail().withSubject("Hi $name").withTo(noNameAddress.getMailAddress()).build();

        MvcResult mvcResult = getPerform(mailInfo)
                .andExpect(view().name("send")).andReturn();

        assertErrorMessage(mvcResult, "", WHEN_YOU_USE_TEMPLATE_CHOOSE_EMAIL_FROM_CONTRACT_LIST_THAT_HAS_A_NAME);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void notBodyReplaceWhenNoNameAddress() throws Exception {

        contactListService.add(noNameAddress);

        MailInfo mailInfo = validMail().withBody("Hi $name").withTo(noNameAddress.getMailAddress()).build();

        MvcResult mvcResult = getPerform(mailInfo)
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "", WHEN_YOU_USE_TEMPLATE_CHOOSE_EMAIL_FROM_CONTRACT_LIST_THAT_HAS_A_NAME);
        verify(mailService, never()).sendMultiple(any());
    }

    @Test
    public void mailServerHasDown()  throws Exception {
        doThrow(new Exception(TRY_TO_SEND_EMAIL_BUT_FAILED)).when(mailService).sendMultiple(any(List.class));
        MvcResult mvcResult = getPerform(validMail().withTo("abc@gmail.com;john@gmail.com").build())
                .andExpect(view().name("send"))
                .andReturn();

        assertErrorMessage(mvcResult, "", TRY_TO_SEND_EMAIL_BUT_FAILED);
        verify(mailService, times(1)).sendMultiple(any());
    }

    private ResultActions getPerform(MailInfo mailInfo) throws Exception {
        return mvc.perform(post("/send")
                .param("from", mailInfo.getFrom())
                .param("address", mailInfo.getTo())
                .param("subject", mailInfo.getSubject())
                .param("body", mailInfo.getBody()));
    }

    private void assertErrorMessage(MvcResult mvcResult, String errorMessage, String errorTemplateMessage) {
        ModelAndView mav = mvcResult.getModelAndView();
        List<ObjectError> objectErrors = ((BindingResult) mav.getModel().get(
                "org.springframework.validation.BindingResult.form")).getAllErrors();


        if (objectErrors.stream().filter(i -> i instanceof FieldError).count() > 0) {
            assertTrue(objectErrors.stream().map(i -> ((FieldError) i))
                    .anyMatch(i -> i.getField() == null ?
                            i.getDefaultMessage().equals(errorTemplateMessage) :
                            i.getField().equals(errorMessage) && i.getDefaultMessage().equals(errorTemplateMessage)));
        } else {
            assertTrue(objectErrors.stream()
                    .anyMatch(i -> i.getDefaultMessage().equals(errorTemplateMessage)));
        }


    }
}
