package pro.craftlab.voucher.endpoint.rest.controller.validator;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pro.craftlab.voucher.endpoint.rest.model.Customer;
import pro.craftlab.voucher.repository.function.EmailValidationSupplier;
import pro.craftlab.voucher.repository.model.exception.BadRequestException;

@Component
public class CustomerRestValidator implements Consumer<Customer> {

  private final EmailValidationSupplier emailValidationSupplier;

  @Autowired
  public CustomerRestValidator(EmailValidationSupplier emailValidationSupplier) {
    this.emailValidationSupplier = emailValidationSupplier;
  }

  @Override
  public void accept(Customer customer) {
    StringBuilder sb = new StringBuilder();
    if (customer == null) {
      throw new BadRequestException("Customer is mandatory");
    } else {
      if (!emailValidationSupplier.isValidEmail(customer.getMail())) {
        sb.append("Invalid email address " + customer.getMail() + ". ");
      }
      if (customer.getName() == null) {
        sb.append("Name is mandatory. ");
      }
    }
    String message = sb.toString();
    if (!message.isEmpty()) {
      throw new RuntimeException(message);
    }
  }
}
