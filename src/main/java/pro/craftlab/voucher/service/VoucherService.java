package pro.craftlab.voucher.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.VoucherRepository;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;

@Service
@AllArgsConstructor
public class VoucherService {

  private static final String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int voucher_length = 10;
  private static SecureRandom random = new SecureRandom();

  @Autowired private VoucherRepository voucherRepository;
  @Autowired private CustomerRepository customerRepository;

  private String generateVoucherCode() {
    StringBuilder voucherCode = new StringBuilder(voucher_length);
    for (int i = 0; i < voucher_length; i++) {
      voucherCode.append(character.charAt(random.nextInt(character.length())));
    }
    return voucherCode.toString();
  }

  public Voucher generateVoucherCodeForCustomer(String idCustomer) {
    Customer customer =
        customerRepository
            .findById(idCustomer)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    String code = generateVoucherCode();
    Instant now = Instant.now();
    Instant validation = now;
    Instant expiration = now.plus(Duration.ofDays(30));
    Instant creationDatetime = now;
    Voucher voucher =
        Voucher.builder()
            .code(code)
            .validation(validation)
            .expiration(expiration)
            .creationDatetime(creationDatetime)
            .customer(customer)
            .build();
    voucherRepository.save(voucher);
    return voucher;
  }
}
