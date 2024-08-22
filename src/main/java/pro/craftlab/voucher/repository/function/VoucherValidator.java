package pro.craftlab.voucher.repository.function;

import static pro.craftlab.voucher.repository.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

import java.time.Instant;
import java.util.function.Consumer;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.model.CreateVoucher;
import pro.craftlab.voucher.repository.model.exception.ApiException;

@Component
public class VoucherValidator implements Consumer<CreateVoucher> {

  public boolean isValidDate(Instant date) {
    if (date == null) {
      throw new ApiException(CLIENT_EXCEPTION, "Email cannot be null or empty");
    }
    return date.isAfter(Instant.now());
  }

  @Override
  public void accept(CreateVoucher voucher) {
    if (voucher.getValidationDatetime() == null) {
      throw new ApiException(CLIENT_EXCEPTION, "Date validation cannot be null or empty");
    }
    if (voucher.getValidationDatetime().isBefore(Instant.now())) {
      throw new ApiException(CLIENT_EXCEPTION, "Date Validation invalid");
    }
  }
}
