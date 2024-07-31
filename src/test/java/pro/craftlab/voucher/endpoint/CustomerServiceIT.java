package pro.craftlab.voucher.endpoint;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.service.event.CustomerService;
import pro.craftlab.voucher.service.event.VoucherService;

@Testcontainers
public class CustomerServiceIT extends FacadeIT {

  @Autowired CustomerService subject;

  @Autowired VoucherService voucherService;
  private static final String id = "customer-id-1";
  Instant creationDatetime = Instant.parse("2024-08-01T10:00:00Z");

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
    assertEquals(1, actual.size());
  }

  @Test
  void read_customer_voucher() {
    Customer customer = expected();
    subject.saveAll(List.of(customer));
    Voucher generatedVoucher = voucherService.generateVoucherCodeForCustomer(customer.getId());
    var actual = subject.getCustomers(Pageable.ofSize((500)));
    assertEquals(1, actual.size());
    System.out.println("Generated Voucher Code: " + generatedVoucher.getCode());
  }

  @Test
  void read_customersById() {
    var actual = subject.getCustomerById(id);
    assertEquals(expected(), actual);
  }

  @Test
  void generate_voucher_for_customer() {
    Customer customer = expected();
    subject.saveAll(List.of(customer));
    Voucher generatedVoucher = voucherService.generateVoucherCodeForCustomer(customer.getId());
    assertEquals(customer.getId(), generatedVoucher.getCustomer().getId());
    assertTrue(generatedVoucher.getValidation().isBefore(generatedVoucher.getExpiration()));
    System.out.println("Generated Voucher: " + generatedVoucher);
  }
}
