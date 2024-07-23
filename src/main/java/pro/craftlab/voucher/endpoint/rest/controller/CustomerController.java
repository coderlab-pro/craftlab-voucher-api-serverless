package pro.craftlab.voucher.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.craftlab.voucher.PojaGenerated;
import pro.craftlab.voucher.endpoint.rest.controller.mapper.CustomerRestMapper;
import pro.craftlab.voucher.endpoint.rest.model.Customer;
import pro.craftlab.voucher.service.event.CustomerService;

@PojaGenerated
@RestController
@AllArgsConstructor
public class CustomerController {
  private CustomerService customerService;
  private CustomerRestMapper customerRestMapper;

  @GetMapping("/customer/{id}")
  public Customer getCustomerById(@PathVariable String id) {
    return customerRestMapper.toRest(customerService.getCustomerById(id));
  }

  @PutMapping("/customers")
  public List<Customer> updateCustomer(@RequestBody List<Customer> customerDetails) {
    var customers = customerDetails.stream().map(customerRestMapper::toDomain).toList();
    return customerService.saveAll(customers).stream().map(customerRestMapper::toRest).toList();
  }
}
