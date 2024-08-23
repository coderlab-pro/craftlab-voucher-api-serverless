package pro.craftlab.voucher.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.event.EventProducer;
import pro.craftlab.voucher.endpoint.event.model.customer.CustomerCreated;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.function.EmailValidator;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.exception.BadRequestException;
import pro.craftlab.voucher.repository.model.exception.NotFoundException;

@Service
@AllArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final EventProducer eventProducer;
  private EmailValidator emailValidator;

  public List<Customer> getCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable).stream().toList();
  }

  public List<Customer> saveAll(List<Customer> customers) {
    for (Customer customer : customers) {
      if (customer == null) {
        throw new BadRequestException("Customer is mandatory");
      }
      emailValidator.accept(customer);
    }
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
