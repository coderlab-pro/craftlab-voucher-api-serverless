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
import pro.craftlab.voucher.repository.model.Voucher;

@Testcontainers
class VoucherServiceIT extends FacadeIT {

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
  void read_customer_voucher() {
    Customer customer = expected();
    subject.saveAll(List.of(customer));
    Voucher generatedVoucher = voucherService.generateVoucherCodeForCustomer(customer.getId());
    var actual = subject.getCustomers(Pageable.ofSize((500)));

    assertEquals(1, actual.size());
  }

  @Test
  void save_customers_and_generate_voucher() {
    var savedCustomers = subject.saveAll(List.of(updatedCustomer()));
    for (Customer newCustomers : savedCustomers) {
      var actual = voucherService.generateVoucherCodeForCustomer(newCustomers.getId());

      assertEquals(10, actual.getCode().length());
    }
  }

  @Test
  void generate_voucher_for_customer() {
    Customer customer = expected();
    subject.saveAll(List.of(customer));
    voucherService.generateVoucherCodeForCustomer(customer.getId());
    Customer actual = subject.getCustomerById(expected().getId());

    assertEquals(1, actual.getVouchers().size());
  }
}
