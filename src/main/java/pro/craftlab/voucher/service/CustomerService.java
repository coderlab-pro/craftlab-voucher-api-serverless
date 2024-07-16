package pro.craftlab.voucher.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.model.Customer;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> getCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).stream().toList();
    }
}
