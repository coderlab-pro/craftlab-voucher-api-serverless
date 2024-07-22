package pro.craftlab.voucher.service.event;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Customer;

@Service
@AllArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;

  public List<Customer> getCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable).stream().toList();
  }

  public Customer createCustomer(Customer customer) {
    return customerRepository.save(customer);
  }

  public Customer updateCustomer(String id, Customer customerDetails) {
    Customer customer;
    customer =
        customerRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
    return customerRepository.save(customer);
  }

  public Customer getCustomerById(String id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }
}
