package pro.craftlab.voucher.repository.function;

import java.security.SecureRandom;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class VoucherCodeGenerator implements Supplier<String> {

  private static final String CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int VOUCHER_LENGTH = 10;
  private static final SecureRandom RANDOM = new SecureRandom();

  @Override
  public String get() {
    StringBuilder voucherCode = new StringBuilder(VOUCHER_LENGTH);
    for (int i = 0; i < VOUCHER_LENGTH; i++) {
      voucherCode.append(CHARACTER.charAt(RANDOM.nextInt(CHARACTER.length())));
    }
    return voucherCode.toString();
  }
}
