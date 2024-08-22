package pro.craftlab.voucher.repository.function;

import java.time.Instant;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.repository.model.exception.BadRequestException;

@Component
public class VoucherValidation {

  public boolean isValidDate(Instant date) {
    if (date == null) {
      throw new BadRequestException("Date Validation invalid");
    }
    return date.isAfter(Instant.now());
  }
}
