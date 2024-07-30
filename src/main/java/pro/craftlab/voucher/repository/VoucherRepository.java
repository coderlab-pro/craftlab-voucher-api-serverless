package pro.craftlab.voucher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {}
