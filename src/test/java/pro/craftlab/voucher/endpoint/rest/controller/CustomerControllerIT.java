package pro.craftlab.voucher.endpoint.rest.controller;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.endpoint.event.EventProducer;
import pro.craftlab.voucher.endpoint.rest.model.Customer;

class CustomerControllerIT extends FacadeIT {
  @MockBean EventProducer eventProducer;
  @Autowired CustomerController subject;

  @Test
  void create_and_read_ok() {
    var actual =
        subject.saveCustomers(
            List.of(
                new Customer().id(randomUUID().toString()).name("John Doe").mail("john@mail.com")));
    var actualCustomers = subject.getCustomers(null, null);

    assertTrue(actualCustomers.containsAll(actual));
  }
}
