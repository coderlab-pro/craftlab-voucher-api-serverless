package pro.craftlab.voucher.endpoint.rest.controller.validator;

import static pro.craftlab.voucher.repository.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.repository.model.exception.ApiException;

@Component
public class EmailRestValidator
    implements Consumer<pro.craftlab.voucher.endpoint.rest.model.Customer> {

  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private final Pattern pattern;

  public EmailRestValidator() {
    this.pattern = Pattern.compile(EMAIL_PATTERN);
  }

  @Override
  public void accept(pro.craftlab.voucher.endpoint.rest.model.Customer customer) {
    if (customer.getMail() == null || customer.getMail().isEmpty()) {
      throw new ApiException(CLIENT_EXCEPTION, "Email cannot be null or empty");
    }
    if (!pattern.matcher(customer.getMail()).matches()) {
      throw new ApiException(CLIENT_EXCEPTION, "Email is not valid");
    }
  }
}
