package pro.craftlab.voucher.endpoint.rest.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.VoucherRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.validator.CustomerRestValidator;
import pro.craftlab.voucher.endpoint.rest.controller.validator.EmailRestValidator;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.repository.model.exception.*;
import pro.craftlab.voucher.service.CustomerService;
import pro.craftlab.voucher.service.VoucherService;

class CustomerControllerTestException {
  CustomerService customerServiceMock = mock();
  EmailRestValidator emailRestValidator = mock();
  VoucherService voucherServiceMock = mock();
  CustomerRestValidator customerRestValidator = new CustomerRestValidator(emailRestValidator);
  CustomerRestMapper customerRestMapper = new CustomerRestMapper(customerRestValidator);
  VoucherRestMapper voucherRestMapper = new VoucherRestMapper();
  CustomerController subject =
      new CustomerController(
          customerServiceMock, customerRestMapper, voucherServiceMock, voucherRestMapper);

  @Test
  void get_customers_vouchers_ko() {
    String customerId = "customerId";
    String expectedMessage = "Customer not found";
    when(customerServiceMock.getCustomerById(customerId))
        .thenThrow(new NotFoundException(expectedMessage));
    NotFoundException thrownException =
        assertThrows(
            NotFoundException.class,
            () -> {
              subject.getCustomerVouchers(customerId);
            });

    assertEquals(expectedMessage, thrownException.getMessage());
  }

  @Test
  void get_customers_ko() {
    String customerId = "customerId";
    String expectedMessage = "Customer with ID " + customerId + " not found";
    when(customerServiceMock.getCustomerById(customerId))
        .thenThrow(new NotFoundException(expectedMessage));
    NotFoundException thrownException =
        assertThrows(
            NotFoundException.class,
            () -> {
              subject.getCustomerById(customerId);
            });

    assertEquals(expectedMessage, thrownException.getMessage());
  }

  @Test
  void generate_voucher_customer_ko() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(-30)));
    createVoucher.setCreationDatetime(Instant.now());
    String customerId = "customerId";
    when(voucherServiceMock.generateVoucherCodeForCustomer(customerId, List.of(createVoucher)))
        .thenThrow(new NotFoundException("Customer not found"));
    NotFoundException thrown =
        assertThrows(
            NotFoundException.class,
            () -> {
              subject.generateVouchersForCustomer(customerId, List.of(createVoucher));
            });

    assertEquals("Customer not found", thrown.getMessage());
  }

  @Test
  void get_customers_vouchers_forbidden_exception() {
    String customerId = "forbiddenCustomerId";
    when(customerServiceMock.getCustomerById(customerId))
        .thenThrow(new ForbiddenException("Access Denied"));

    assertThrows(ForbiddenException.class, () -> subject.getCustomerVouchers(customerId));
  }

  @Test
  void get_all_customers_forbidden_exception() {
    when(customerServiceMock.getCustomers(any(PageRequest.class)))
        .thenThrow(new ForbiddenException("Access Denied"));
    ForbiddenException thrown =
        assertThrows(
            ForbiddenException.class,
            () -> {
              subject.getCustomers(1, 1);
            });

    assertEquals("Access Denied", thrown.getMessage());
  }

  @Test
  void get_all_customers_too_many_requests() {
    when(customerServiceMock.getCustomers(any(PageRequest.class)))
        .thenAnswer(
            invocation -> {
              throw new TooManyRequestsException("Too many requests");
            });
    TooManyRequestsException thrownException =
        assertThrows(
            TooManyRequestsException.class,
            () -> {
              for (int i = 0; i < 100; i++) {
                subject.getCustomers(1, 1);
              }
            });

    assertEquals("Too many requests", thrownException.getMessage());
  }

  @Test
  void generate_voucher_customer_too_many_requests() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());
    String customerId = "customerId";
    when(voucherServiceMock.generateVoucherCodeForCustomer(customerId, List.of(createVoucher)))
        .thenThrow(new TooManyRequestsException("Too many requests"));
    TooManyRequestsException thrown =
        assertThrows(
            TooManyRequestsException.class,
            () -> subject.generateVouchersForCustomer(customerId, List.of(createVoucher)));

    assertEquals("Too many requests", thrown.getMessage());
  }

  @Test
  void update_customers_email_invalid() {
    var customerDetails =
        new pro.craftlab.voucher.endpoint.rest.model.Customer()
            .id("customerId")
            .name("Paul")
            .mail("invalid-email");
    doThrow(new BadRequestException("Invalid email address"))
        .when(emailRestValidator)
        .accept(customerDetails);
    when(customerServiceMock.saveAll(any()))
        .thenThrow(new BadRequestException("Invalid email address"));
    BadRequestException thrownException =
        assertThrows(
            BadRequestException.class, () -> subject.saveCustomers(List.of(customerDetails)));
    assertTrue(thrownException.getMessage().contains("Invalid email address"));

    verify(emailRestValidator).accept(customerDetails);
  }
}
