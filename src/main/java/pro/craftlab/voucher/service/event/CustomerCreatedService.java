package pro.craftlab.voucher.service.event;

import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.event.model.customer.CustomerCreated;
import pro.craftlab.voucher.mail.Email;
import pro.craftlab.voucher.mail.Mailer;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerCreatedService extends CustomerCreated implements Consumer<CustomerCreated> {
  private final Mailer mailer;

  // TODO: add thymeleaf parser

  @SneakyThrows
  @Override
  public void accept(CustomerCreated customerCreated) {
    var customer = customerCreated.getCustomer();
    var email = customer.getMail();
    InternetAddress to = new InternetAddress(email);
    List<InternetAddress> cc = List.of(new InternetAddress("contact@craftlab.pro"));
    List<InternetAddress> bcc = List.of();
    mailer.accept(new Email(to, cc, bcc, "Your new voucher code is now available", "", List.of()));
    log.info("Customer creation processing finished, mail sent to " + email);
  }
}
