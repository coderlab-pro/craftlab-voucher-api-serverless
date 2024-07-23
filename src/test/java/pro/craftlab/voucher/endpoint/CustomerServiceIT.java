package pro.craftlab.voucher.endpoint;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.service.event.CustomerService;

public class CustomerServiceIT extends FacadeIT {
  @Autowired CustomerService subject;
  private static final String id = "customer-id-1";

  Customer expected() {
    return new Customer("customer-id-1", "Paul Updated", "paul.updated@gmail.com");
  }

  Customer updated() {
    return new Customer("customer-id-1", "Paul", "paul@gmail.com");
  }

  @Test
  void read_customers() {
    var actual = subject.getCustomers(Pageable.ofSize((500)));
    assertEquals(1, actual.size());
    assertTrue(actual.contains(expected()));
  }

  @Test
  void save_customers() {
    var actual = subject.saveAll(List.of(updated()));
  }
}
