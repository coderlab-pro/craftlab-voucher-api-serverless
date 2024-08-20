package pro.craftlab.voucher.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.event.EventProducer;
import pro.craftlab.voucher.endpoint.event.model.customer.CustomerCreated;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.exception.NotFoundException;

@Service
@AllArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final EventProducer eventProducer;

  public List<Customer> getCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable).stream().toList();
  }

  public List<Customer> saveAll(List<Customer> customers) {
    customers.forEach(
        customer -> {
          var optionalCustomer = customerRepository.findById(customer.getId());
          if (optionalCustomer.isEmpty()) {
            eventProducer.accept(List.of(CustomerCreated.builder().customer(customer).build()));
          }
        });
    return customerRepository.saveAll(customers);
  }

  public Customer getCustomerById(String id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException("Customer not found" + id));
  }
}
