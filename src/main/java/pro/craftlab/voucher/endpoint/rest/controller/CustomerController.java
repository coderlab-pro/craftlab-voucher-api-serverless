package pro.craftlab.voucher.endpoint.rest.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.craftlab.voucher.PojaGenerated;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.service.event.CustomerService;

@PojaGenerated
@RestController
@AllArgsConstructor
public class CustomerController {

  private final PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer;
  private CustomerRepository customerRepository;
  private CustomerService customerService;

  @GetMapping("/customer/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable String id) {
    Customer customer = customerService.getCustomerById(id);
    return ResponseEntity.ok(customer);
  }

  @PostMapping
  public Customer createCustomer(@RequestBody Customer customer) {
    return customerService.createCustomer(customer);
  }

  @PutMapping("/customers/{id}")
  public ResponseEntity<Customer> updateCustomer(
      @PathVariable String id, @RequestBody Customer customerDetails) {
    Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
    updatedCustomer.setName(customerDetails.getName());
    updatedCustomer.setMail(customerDetails.getMail());
    return ResponseEntity.ok(updatedCustomer);
  }
}
