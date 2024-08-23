package pro.craftlab.voucher.service;

import static java.time.Instant.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import pro.craftlab.voucher.conf.FacadeIT;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.repository.model.exception.ApiException;
import pro.craftlab.voucher.repository.model.exception.NotFoundException;

@Transactional(isolation = SERIALIZABLE)
class VoucherServiceIT extends FacadeIT {
  @Autowired CustomerService customerService;
  @Autowired VoucherService subject;

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
  void create_and_read_customer_voucher() {
    var createVoucher =
        new CreateVoucher()
            .validationDatetime(now().plus(Duration.ofDays(30)))
            .creationDatetime(now());
    var customer = expected();
    customerService.saveAll(List.of(customer));

    var actual = subject.generateVoucherCodeForCustomer(customer.getId(), List.of(createVoucher));

    var retrievedVouchers = customerService.getCustomerById(customer.getId()).getVouchers();
    assertTrue(retrievedVouchers.contains(actual));
  }

  @Test
  void save_customers_and_manage_invalid_customer() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(now());
    Customer validCustomer = updatedCustomer();
    Customer invalidCustomer =
        Customer.builder()
            .id("invalidCustomerId")
            .name("paul")
            .mail("invalid-email")
            .vouchers(Set.of())
            .build();
    customerService.saveAll(List.of(validCustomer));

    Voucher actual =
        subject.generateVoucherCodeForCustomer(validCustomer.getId(), List.of(createVoucher));
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              customerService.saveAll(List.of(invalidCustomer));
            });

    assertNotNull(actual);
    assertEquals(10, actual.getCode().length());
    assertTrue(exception.getMessage().contains("Email is not valid"), "IEmail is not valid");
  }

  @Test
  void generate_voucher_for_non_existent_customer() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(now());
    NotFoundException exception =
        assertThrows(
            NotFoundException.class,
            () -> subject.generateVoucherCodeForCustomer("noneCustomer", List.of(createVoucher)));

    assertTrue(exception.getMessage().contains("Customer not found"));
  }

  @Test
  void generate_voucher_for_customer_with_invalid_data() {
    CreateVoucher invalidVoucher = new CreateVoucher();
    invalidVoucher.setValidationDatetime(now().plus(Duration.ofDays(-30)));
    invalidVoucher.setCreationDatetime(now());
    Customer validCustomer = updatedCustomer();
    customerService.saveAll(List.of(validCustomer));
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              subject.generateVoucherCodeForCustomer(
                  validCustomer.getId(), List.of(invalidVoucher));
            });

    assertTrue(exception.getMessage().contains("Date Validation invalid"));
  }

  @Test
  void validationDate_exception() {
    CreateVoucher invalidVoucher = new CreateVoucher();
    invalidVoucher.setValidationDatetime(null);
    invalidVoucher.setCreationDatetime(now());
    Customer validCustomer = updatedCustomer();
    customerService.saveAll(List.of(validCustomer));
    ApiException exception =
        assertThrows(
            ApiException.class,
            () -> {
              subject.generateVoucherCodeForCustomer(
                  validCustomer.getId(), List.of(invalidVoucher));
            });

    assertTrue(exception.getMessage().contains("Date validation cannot be null "));
  }
}
