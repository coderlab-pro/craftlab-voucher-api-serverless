package pro.craftlab.voucher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.craftlab.voucher.repository.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}
