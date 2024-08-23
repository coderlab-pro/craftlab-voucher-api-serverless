package pro.craftlab.voucher.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.thymeleaf.context.Context;
import pro.craftlab.voucher.endpoint.event.model.customer.CustomerCreated;
import pro.craftlab.voucher.mail.Email;
import pro.craftlab.voucher.mail.Mailer;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.template.HTMLTemplateParser;

public class CustomerCreatedServiceTest {
  private static final String HTML_BODY = "htmlBody";
  HTMLTemplateParser htmlParserMock = mock();
  Mailer mailerMock = mock();
  CustomerCreatedService subject = new CustomerCreatedService(mailerMock, htmlParserMock);

  @BeforeEach
  void setUp() {
    when(htmlParserMock.apply(eq("onboarding"), any(Context.class))).thenReturn(HTML_BODY);
  }

  @Test
  void send_mail_ok() {
    Customer customer = Customer.builder().name("dummy").mail("dummy@mail.com").build();

    subject.accept(CustomerCreated.builder().customer(customer).build());

    var emailCaptor = ArgumentCaptor.forClass(Email.class);
    verify(mailerMock, times(1)).accept(emailCaptor.capture());
    var emailSent = emailCaptor.getValue();
    assertEquals(customer.getMail(), emailSent.to().getAddress());
    assertEquals("Welcome to craftlab", emailSent.subject());
    assertEquals(HTML_BODY, emailSent.htmlBody());
  }
}
