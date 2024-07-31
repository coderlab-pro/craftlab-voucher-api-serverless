package pro.craftlab.voucher.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.model.Customer;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Voucher;
import pro.craftlab.voucher.service.event.CustomerService;
import pro.craftlab.voucher.service.event.VoucherService;

@RestController
@AllArgsConstructor
public class CustomerController {
  private CustomerService customerService;
  private CustomerRestMapper customerRestMapper;
  private VoucherService voucherGeneratorService;
  private final CustomerRepository customerRepository;

  @GetMapping("/customers/{id}")
  public Customer getCustomerById(@PathVariable String id) {
    return customerRestMapper.toRest(customerService.getCustomerById(id));
  }

  @PutMapping("/customers")
  public List<Customer> updateCustomer(@RequestBody List<Customer> customerDetails) {
    var customers = customerDetails.stream().map(customerRestMapper::toDomain).toList();
    return customerService.saveAll(customers).stream().map(customerRestMapper::toRest).toList();
  }

  @PutMapping("/customers/{id}/vouchers")
  public Voucher generateVouchersForCustomer(@PathVariable String id) {
    return voucherGeneratorService.generateVoucherCodeForCustomer(id);
  }

  @GetMapping("/customers/{id}/vouchers")
  public Customer getCustomerVouchers(@PathVariable String id) {
    var customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return customerRestMapper.toRest(customer);
  }
}
