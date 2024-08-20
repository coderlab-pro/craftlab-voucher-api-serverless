package pro.craftlab.voucher.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.VoucherRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.validator.CustomerRestValidator;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.endpoint.rest.model.Voucher;
import pro.craftlab.voucher.repository.function.EmailValidationSupplier;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.exception.*;
import pro.craftlab.voucher.service.CustomerService;
import pro.craftlab.voucher.service.VoucherService;
import pro.craftlab.voucher.service.event.CustomerCreatedService;

class CustomerControllerTest {
  @Mock private CustomerCreatedService customerCreatedService;
  CustomerService customerServiceMock = mock();
  VoucherService voucherServiceMock = mock();
  EmailValidationSupplier emailValidationSupplier = mock();
  CustomerRestValidator customerRestValidator = new CustomerRestValidator(emailValidationSupplier);
  CustomerRestMapper customerRestMapper = new CustomerRestMapper(customerRestValidator);

  VoucherRestMapper voucherRestMapper = new VoucherRestMapper();
  CustomerController subject =
      new CustomerController(
          customerServiceMock, customerRestMapper, voucherServiceMock, voucherRestMapper);

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

    assertThrows(RuntimeException.class, () -> subject.getCustomerVouchers(customerId));
  }

  @Test
  void get_customers_ko() {
    String customerId = "customerId";
    when(customerServiceMock.getCustomerById(customerId)).thenThrow(BadRequestException.class);

    assertThrows(RuntimeException.class, () -> subject.getCustomerById(customerId));
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

  /*@Test
  void update_customers_ok() {
    var customerDetails =
        List.of(new pro.craftlab.voucher.endpoint.rest.model.Customer().id("customerId").name("Paul").mail("paul@gmail.com"));
    when(customerServiceMock.saveAll(any()))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    var actual = subject.saveCustomers(customerDetails);

    assertEquals(customerDetails, actual);
  }*/

  @Test
  void update_customers_ok() {

    var customerDetails =
        List.of(
            new pro.craftlab.voucher.endpoint.rest.model.Customer()
                .id("customerId")
                .name("Paul")
                .mail("paul@gmail.com"));

    when(emailValidationSupplier.isValidEmail("paul@gmail.com")).thenReturn(true);
    when(customerServiceMock.saveAll(any()))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    var actual = subject.saveCustomers(customerDetails);

    assertEquals(customerDetails, actual);
  }
}
