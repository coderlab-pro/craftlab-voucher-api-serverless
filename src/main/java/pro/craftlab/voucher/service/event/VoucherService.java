package pro.craftlab.voucher.service.event;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.craftlab.voucher.repository.CustomerRepository;
import pro.craftlab.voucher.repository.VoucherRepository;
import pro.craftlab.voucher.repository.model.Customer;
import pro.craftlab.voucher.repository.model.Voucher;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class VoucherService {

  private static final String character = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int voucher_length = 10;
  private static SecureRandom random = new SecureRandom();

  @Autowired   private VoucherRepository voucherRepository;
  @Autowired   private CustomerRepository customerRepository;

  private @NotNull String generateVoucherCode() {
    StringBuilder voucherCode = new StringBuilder(voucher_length);
    for (int i = 0; i < voucher_length; i++) {
      voucherCode.append(character.charAt(random.nextInt(character.length())));
    }
    return voucherCode.toString();
  }

  public @NotNull String generateVoucherCodeForCustomer(String idCustomer) {
    Customer customer = customerRepository.findById(idCustomer)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    String code = generateVoucherCode();
    Voucher voucher = Voucher.builder()
            .code(code)
            .validation(Date.valueOf(LocalDate.now()))
            .expiration(Date.valueOf(LocalDate.now().plusDays(30)))
            .customer(customer)
            .build();
    voucherRepository.save(voucher);
    return code;
  }
}
