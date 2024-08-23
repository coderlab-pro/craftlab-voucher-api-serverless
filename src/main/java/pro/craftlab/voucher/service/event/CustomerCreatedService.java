package pro.craftlab.voucher.service.event;

import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import pro.craftlab.voucher.endpoint.event.model.customer.CustomerCreated;
import pro.craftlab.voucher.mail.Email;
import pro.craftlab.voucher.mail.Mailer;
import pro.craftlab.voucher.repository.function.EmailValidator;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.template.HTMLTemplateParser;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerCreatedService implements Consumer<CustomerCreated> {
  private final Mailer mailer;
  private final HTMLTemplateParser htmlTemplateParser;
  private final EmailValidator emailValidator;

  @SneakyThrows
  @Override
  public void accept(CustomerCreated customerCreated) {

    var customer = customerCreated.getCustomer();
    var email = customer.getMail();
    var to = new InternetAddress(email);
    var cc = List.of(new InternetAddress("contact@craftlab.pro"));
    var bcc = new ArrayList<InternetAddress>();
    var attachments = new ArrayList<File>();
    var subject = "Welcome to craftlab";
    var htmlBody = processHTMLBody(customer);
    emailValidator.accept(customer);
    mailer.accept(new Email(to, cc, bcc, subject, htmlBody, attachments));

    log.info("Customer creation processing finished, mail sent to " + email);
  }

  private String processHTMLBody(Customer customer) {
    Context context = new Context();
    context.setVariable("customer", customer);
    return htmlTemplateParser.apply("onboarding", context);
  }
}
