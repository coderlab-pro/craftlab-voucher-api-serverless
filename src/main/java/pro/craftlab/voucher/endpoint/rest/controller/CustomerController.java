package pro.craftlab.voucher.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.VoucherRestMapper;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.endpoint.rest.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.service.CustomerService;
import pro.craftlab.voucher.service.VoucherService;

@RestController
@AllArgsConstructor
public class CustomerController {
  private CustomerService customerService;
  private CustomerRestMapper customerRestMapper;
  private VoucherService voucherGeneratorService;
  private VoucherRestMapper voucherRestMapper;

  @GetMapping("/customers/{id}")
  public Customer getCustomerById(@PathVariable String id) {
    return customerRestMapper.toRest(customerService.getCustomerById(id));
  }

  @PutMapping("/customers")
  public List<Customer> saveCustomers(@RequestBody List<Customer> customerDetails) {
    var customers = customerDetails.stream().map(customerRestMapper::toDomain).toList();
    return customerService.saveAll(customers).stream().map(customerRestMapper::toRest).toList();
  }

  public List<Voucher> generateVouchersForCustomer(
      @PathVariable String id, @RequestBody List<CreateVoucher> createVouchers) {
    List<Voucher> vouchers =
        createVouchers.stream()
            .map(
                createVoucher ->
                    voucherGeneratorService.generateVoucherCodeForCustomer(
                        id, List.of(createVoucher)))
            .toList();
    return vouchers;
  }

  @GetMapping("/customers/{id}/vouchers")
  public List<pro.craftlab.voucher.endpoint.rest.model.Voucher> getCustomerVouchers(
      @PathVariable String id) {
    var customer = customerService.getCustomerById(id);
    return customer.getVouchers().stream().map(voucherRestMapper::toRest).toList();
  }
}
