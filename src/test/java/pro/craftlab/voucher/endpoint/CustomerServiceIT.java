package pro.craftlab.voucher.endpoint;

import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.service.event.CustomerService;

public class CustomerServiceIT extends FacadeIT {
  @Autowired CustomerService subject;
  private Customer expected() {
    return Customer.builder()
            .id("customer-id-1")
            .name("Paul Updated")
            .mail("paul.updated@gmail.com")
            .vouchers(Set.of())
            .build();
  }

  private Customer updated() {
    return Customer.builder()
            .id("customer-id-1")
            .name("Paul")
            .mail("paul@gmail.com")
            .vouchers(Set.of())
            .build();
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
