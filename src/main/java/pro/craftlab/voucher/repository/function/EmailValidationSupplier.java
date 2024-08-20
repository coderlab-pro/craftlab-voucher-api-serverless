package pro.craftlab.voucher.repository.function;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationSupplier {

  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private final Pattern pattern;

  public EmailValidationSupplier() {
    this.pattern = Pattern.compile(EMAIL_PATTERN);
  }

  public boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
      return false;
    }
    return pattern.matcher(email).matches();
  }
}
