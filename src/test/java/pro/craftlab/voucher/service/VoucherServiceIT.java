package pro.craftlab.voucher.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Testcontainers;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.repository.model.exception.ApiException;
import pro.craftlab.voucher.repository.model.exception.NotFoundException;

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
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());
    Customer customer = expected();
    subject.saveAll(List.of(customer));
    Voucher generatedVoucher =
        voucherService.generateVoucherCodeForCustomer(customer.getId(), List.of(createVoucher));
    var actual = subject.getCustomers(Pageable.ofSize((500)));

    assertEquals(1, actual.size());

    System.out.println("VOUCHER :" + createVoucher);
  }

  @Test
  void save_customers_and_generate_voucher() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());

    Customer customer = updatedCustomer();
    List<Customer> savedCustomers = subject.saveAll(List.of(customer));

    for (Customer newCustomer : savedCustomers) {
      Voucher actual =
          voucherService.generateVoucherCodeForCustomer(
              newCustomer.getId(), List.of(createVoucher));

      assertNotNull(actual, "Voucher should not be null");
      assertEquals(10, actual.getCode().length(), "Voucher code length should be 10 characters");
    }
  }

  @Test
  void generate_voucher_for_customer() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());

    Customer customer = expected();
    subject.saveAll(List.of(customer));

    Voucher firstVoucher =
        voucherService.generateVoucherCodeForCustomer(customer.getId(), List.of(createVoucher));
    assertNotNull(firstVoucher);

    Voucher secondVoucher =
        voucherService.generateVoucherCodeForCustomer(customer.getId(), List.of(createVoucher));
    assertNotNull(secondVoucher);

    Customer updatedCustomer = subject.getCustomerById(customer.getId());

    int voucherCount = updatedCustomer.getVouchers().size();
    assertEquals(2, voucherCount, "Customer should have 2 vouchers generated.");
  }

  @Test
  void save_customers_and_manage_invalid_customer() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());

    Customer validCustomer = updatedCustomer();
    Customer invalidCustomer =
        Customer.builder()
            .id("invalidCustomerId")
            .name("paul")
            .mail("invalid-email")
            .vouchers(Set.of())
            .build();

    List<Customer> savedCustomers = subject.saveAll(List.of(validCustomer));

    Voucher actualVoucher =
        voucherService.generateVoucherCodeForCustomer(
            validCustomer.getId(), List.of(createVoucher));
    assertNotNull(actualVoucher);
    assertEquals(10, actualVoucher.getCode().length());

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              subject.saveAll(List.of(invalidCustomer));
            });
    assertTrue(exception.getMessage().contains("Email is not valid"), "IEmail is not valid");
  }

  @Test
  void generate_voucher_for_nonexistent_customer() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());

    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> {
              voucherService.generateVoucherCodeForCustomer("noneCustomer", List.of(createVoucher));
            });
    assertTrue(exception.getMessage().contains("Customer not found"));
  }

  @Test
  void generate_voucher_for_customer_with_invalid_data() {
    CreateVoucher invalidVoucher = new CreateVoucher();
    invalidVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(-30)));
    invalidVoucher.setCreationDatetime(Instant.now());

    Customer validCustomer = updatedCustomer();
    subject.saveAll(List.of(validCustomer));

    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              voucherService.generateVoucherCodeForCustomer(
                  validCustomer.getId(), List.of(invalidVoucher));
            });
    assertTrue(exception.getMessage().contains("Date Validation invalid"));
  }

  @Test
  void validationDate_exception() {
    CreateVoucher invalidVoucher = new CreateVoucher();
    invalidVoucher.setValidationDatetime(null);
    invalidVoucher.setCreationDatetime(Instant.now());

    Customer validCustomer = updatedCustomer();
    subject.saveAll(List.of(validCustomer));
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              voucherService.generateVoucherCodeForCustomer(
                  validCustomer.getId(), List.of(invalidVoucher));
            });

    assertTrue(exception.getMessage().contains("Date validation cannot be null or empty"));
  }
}
