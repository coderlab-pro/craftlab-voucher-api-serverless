package pro.craftlab.voucher.repository.function;

import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class VoucherValidation {

  public boolean isValidDate(Instant date) {
    if (date == null) {
      throw new IllegalArgumentException("Date cannot be null");
    }
    return date.isAfter(Instant.now());
  }
}
