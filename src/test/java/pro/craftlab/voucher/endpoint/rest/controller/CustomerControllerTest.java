package pro.craftlab.voucher.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.VoucherRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.validator.CustomerRestValidator;
import pro.craftlab.voucher.endpoint.rest.controller.validator.EmailRestValidator;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.endpoint.rest.model.Voucher;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.exception.*;
import pro.craftlab.voucher.service.CustomerService;
import pro.craftlab.voucher.service.VoucherService;

class CustomerControllerTest {
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
  void get_all_customers() {
    Customer customer1 = new Customer();
    customer1.setId("customer1");
    customer1.setName("John");
    customer1.setMail("john@example.com");

    List<Customer> expected = List.of(customer1);

    when(customerServiceMock.getCustomers(any(PageRequest.class))).thenReturn(expected);
    List<pro.craftlab.voucher.endpoint.rest.model.Customer> actual = subject.getCustomers(1, 1);
    assertEquals(expected.size(), actual.size());

    Assertions.assertTrue(
        expected.stream()
            .allMatch(
                e ->
                    actual.stream()
                        .anyMatch(
                            a ->
                                e.getId().equals(a.getId())
                                    && e.getName().equals(a.getName())
                                    && e.getMail().equals(a.getMail()))),
        "Some customers do not match");
  }

  @Test
  void get_customers_vouchers_ok() {
    String customerId = "customerId";
    when(customerServiceMock.getCustomerById(customerId))
        .thenReturn(Customer.builder().vouchers(Set.of()).build());
    List<Voucher> exepted = List.of();

    List<Voucher> actual = subject.getCustomerVouchers(customerId);

    assertEquals(exepted, actual);
  }

  @Test
  void get_customers_vouchers_ko() {
    String customerId = "customerId";
    when(customerServiceMock.getCustomerById(customerId)).thenThrow(BadRequestException.class);

    assertThrows(BadRequestException.class, () -> subject.getCustomerVouchers(customerId));
  }

  @Test
  void generate_voucher_customer_ok() {
    CreateVoucher createVoucher = new CreateVoucher();
    createVoucher.setValidationDatetime(Instant.now().plus(Duration.ofDays(30)));
    createVoucher.setCreationDatetime(Instant.now());
    String customerId = "customerId";
    when(voucherServiceMock.generateVoucherCodeForCustomer(customerId, List.of(createVoucher)))
        .thenReturn(pro.craftlab.voucher.repository.model.Voucher.builder().build());
    List<pro.craftlab.voucher.repository.model.Voucher> exepted =
        List.of(pro.craftlab.voucher.repository.model.Voucher.builder().build());

    var actual = subject.generateVouchersForCustomer(customerId, List.of(createVoucher));

    assertEquals(exepted, actual);
  }

  @Test
  void update_customers_ok() {
    var customerDetails =
        new pro.craftlab.voucher.endpoint.rest.model.Customer()
            .id("customerId")
            .name("Paul")
            .mail("paul@gmail.com");

    doNothing()
        .when(emailRestValidator)
        .accept(any(pro.craftlab.voucher.endpoint.rest.model.Customer.class));

    when(customerServiceMock.saveAll(any()))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    var actual = subject.saveCustomers(List.of(customerDetails));

    assertEquals(List.of(customerDetails), actual);
  }
}
