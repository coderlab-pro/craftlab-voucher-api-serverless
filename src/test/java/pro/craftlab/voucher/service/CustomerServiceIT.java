package pro.craftlab.voucher.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.repository.model.Customer;

@Testcontainers
class CustomerServiceIT extends FacadeIT {

  @Autowired CustomerService subject;
  @Autowired VoucherService voucherService;
  private static final String id = "customer-id-1";

  private Customer expected() {
    return Customer.builder()
        .id("customer-id-1")
        .name("Paul Updated")
        .mail("paul.updated@gmail.com")
        .vouchers(Set.of())
        .build();
  }

  private Customer updatedCustomer() {
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

    assertTrue(actual.contains(expected()));
    assertEquals(1, actual.size());
  }

  @Test
  void read_customersById() {
    Customer expectedCustomer = updatedCustomer();
    var savedCustomers = subject.saveAll(List.of(updatedCustomer()));
    assertEquals(1, savedCustomers.size());

    Customer actual = subject.getCustomerById(expectedCustomer.getId());

    assertEquals(expectedCustomer, actual);
  }
}
