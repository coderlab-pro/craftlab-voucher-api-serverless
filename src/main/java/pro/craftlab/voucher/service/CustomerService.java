package pro.craftlab.voucher.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.endpoint.event.EventProducer;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Customer;

@Service
@AllArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final EventProducer eventProducer;

  public List<Customer> getCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable).stream().toList();
  }

  public List<Customer> saveAll(List<Customer> customers) {
    // TODO: check if customer is already saved or to be updated then use eventProducer
    return customerRepository.saveAll(customers);
  }

  public Customer getCustomerById(String id) {
    return customerRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Customer not found"));
  }
}
