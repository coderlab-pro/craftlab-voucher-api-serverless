package pro.craftlab.voucher.endpoint.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.VoucherRestMapper;
import pro.craftlab.voucher.endpoint.rest.model.Voucher;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.service.CustomerService;
import pro.craftlab.voucher.service.VoucherService;

class CustomerControllerTest {
  CustomerService customerServiceMock = mock();
  VoucherService voucherServiceMock = mock();
  CustomerRestMapper customerRestMapper = new CustomerRestMapper();
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
    when(customerServiceMock.getCustomerById(customerId)).thenThrow(RuntimeException.class);

    assertThrows(RuntimeException.class, () -> subject.getCustomerVouchers(customerId));
  }

  @Test
  void generate_voucher_customer_ok() {
    String customerId = "customerId";
    when(voucherServiceMock.generateVoucherCodeForCustomer(customerId))
        .thenReturn(pro.craftlab.voucher.repository.model.Voucher.builder().build());
    List<pro.craftlab.voucher.repository.model.Voucher> exepted =
        List.of(pro.craftlab.voucher.repository.model.Voucher.builder().build());

    var actual = subject.generateVouchersForCustomer(customerId);

    assertEquals(exepted, actual);
  }

  @Test
  void update_customers_ok() {
    var customerDetails =
        List.of(new pro.craftlab.voucher.endpoint.rest.model.Customer().id("customerId"));
    when(customerServiceMock.saveAll(any()))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

    var actual = subject.saveCustomers(customerDetails);

    assertEquals(customerDetails, actual);
  }
}
