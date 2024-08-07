package pro.craftlab.voucher.service;

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

  public List<Customer> saveAll(List<Customer> customers) {
    return customerRepository.saveAll(customers);
  }

  public Customer getCustomerById(String id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }
}
